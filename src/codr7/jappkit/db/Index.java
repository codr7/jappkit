package codr7.jappkit.db;

import codr7.jappkit.Cmp;
import codr7.jappkit.E;
import codr7.jappkit.error.EOF;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.nio.file.Path;
import java.util.stream.Stream;

public class Index extends Relation {
    public Index(Schema schema, String name, Col<?>...cols) {
        super(schema, name);
        schema.addIndex(this);
        for (Col<?> c: cols) { addCol(c); }
    }

    @Override
    public Index addCol(Col<?> it) {
        cols.add(it);
        return this;
    }

    @Override
    public Stream<Col<?>> cols() { return cols.stream(); }

    @Override
    public void open(Instant maxTime) {
        try {
            Path dataPath = Path.of(schema.root.toString(), name + ".idx");
            file = Files.newByteChannel(dataPath, fileOptions);
        } catch (IOException e) { throw new E(e); }

        try {
            for (; ; ) {
                Instant ts = Encoding.readTime(file);
                if (ts.compareTo(maxTime) > 0) { break; }
                Object[] key = new Object[cols.size()+1];
                for (int i = 0; i < key.length-1; i++) { key[i] = cols.get(i).load(file); }
                long recordId = Encoding.readLong(file);
                if (recordId == -1L) { recs.remove(key); } else { recs.put(key, recordId); }
            }
        } catch (EOF e) { }
    }

    @Override
    public void close() {
        try { file.close(); } catch (IOException e) { throw new E(e); }
        file = null;
        recs.clear();
    }

    public int compareKeys(Object[] x, Object[] y) {
        for (int i = 0; i < cols.size(); i++) {
            if (x.length == i && y.length != i) {
                return Cmp.LT.asInt;
            }

            if (y.length == i) {
                return (x.length == i) ? Cmp.EQ.asInt : Cmp.GT.asInt;
            }

            var c = cols.get(i);
            var result = c.cmpObject(x[i], y[i]);
            if (result != Cmp.EQ) { return result.asInt; }
        }

        return Cmp.EQ.asInt;
    }

    public Object[] key(Rec it) {
        var out = new Object[cols.size()];
        int i = 0;
        for (var c: cols) { out[i++] = it.getObject(c); }
        return out;
    }

    public <ValueT> ValueT key(Object[] key, Col<ValueT> col) {
        int i = cols.indexOf(col);
        if (i == -1) { throw new E("Invalid key column: %s", col.name); }
        return (ValueT)key[i];
    }

    public Long find(Object[] key, Tx tx) {
        var out = tx.get(this, key);
        if (out == null) { out = recs.get(key); } else if (out == -1L) { out = null; }
        return out;
    }

    public Stream<Map.Entry<Object[], Long>> findFirst(Object[] key, Tx tx) {
        var rs = recs
                .subMap(key, true, recs.lastKey(), true)
                .entrySet()
                .stream()
                .filter((i) -> !tx.isRemoved(this, i.getKey()));

        var txrs = tx.findFirst(this, key);
        return Stream.concat(rs, txrs).sorted((x, y) -> compareKeys(x.getKey(), y.getKey())).distinct();
    }

    @Override
    public void init(Rec it, Col<?>...cols) {
        for (var c: (cols.length == 0) ? this.cols.toArray(cols) : cols) {
            if (!it.contains(c)) { it.setObject(c, c.initObject()); }
        }
    }

    public void commit(Object[] key, long recId) {
        synchronized(file) {
            Encoding.writeTime(Instant.now(), file);
            for (int i = 0; i < cols.size(); i++) { cols.get(i).store(key[i], file); }
            Encoding.writeLong(recId, file);
        }

        if (recId == -1L) { recs.remove(key); } else { recs.put(key, recId); }
    }

    public void add(Rec it, long id, Tx tx) {
        var k = key(it);
        var txr = tx.get(this, k);
        if ((recs.containsKey(k) && (txr == null || txr != -1L)) || (txr != null && txr != -1L)) { throw new E("Duplicate key in index '%s'", name); }
        tx.put(this, k, id);
    }

    public boolean remove(Rec it, Tx tx) { return tx.remove(this, key(it)); }

    public Stream<Map.Entry<Object[], Long>> records(Tx tx) {
        var rs = recs
                .entrySet()
                .stream()
                .filter((i) -> !tx.isRemoved(this, i.getKey()));

        return Stream.concat(rs, tx.records(this)).distinct();
    }

    private SeekableByteChannel file;
    private final List<Col<?>> cols = new ArrayList<>();
    private final ConcurrentSkipListMap<Object[], Long> recs = new ConcurrentSkipListMap<>(this::compareKeys);
}
