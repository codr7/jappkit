package codr7.jappkit.db;

import codr7.jappkit.db.errors.EIO;

import java.io.EOFException;
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
    public Index(Schema schema, String name, Column<?>...cols) {
        super(schema, name);
        schema.addIndex(this);
        for (Column<?> c: cols) { addColumn(c); }
    }

    @Override
    public Index addColumn(Column<?> it) {
        columns.add(it);
        return this;
    }

    @Override
    public void open(Instant maxTime) {
        try {
            Path dataPath = Path.of(schema.root.toString(), name + ".idx");
            file = Files.newByteChannel(dataPath, fileOptions);
        } catch (IOException e) { throw new EIO(e); }

        try {
            for (; ; ) {
                Instant ts = Encoding.readTime(file);
                if (ts.compareTo(maxTime) > 0) { break; }
                Object[] key = new Object[columns.size()];
                for (int i = 0; i < key.length; i++) { key[i] = columns.get(i).load(file); }
                long recordId = Encoding.readLong(file);
                if (recordId == -1L) { records.remove(key); } else { records.put(key, recordId); }
            }
        } catch (EIO e) {
            if (e.getCause().getClass() != EOFException.class) { throw e; }
        }
    }

    @Override
    public void close() {
        try { file.close(); } catch (IOException e) { throw new EIO(e); }
        file = null;
        records.clear();
    }

    public int compareKeys(Object[] x, Object[] y) {
        int i = 0;

        for (Column<?> c: columns) {
            if (x.length == i && y.length != i) {
                return Cmp.LT.asInt;
            }

            if (y.length == i) {
                return (x.length == i) ? Cmp.EQ.asInt : Cmp.GT.asInt;
            }

            Cmp result = c.cmpObject(x[i], y[i]);
            if (result != Cmp.EQ) { return result.asInt; }
            i++;
        }

        return Cmp.EQ.asInt;
    }

    public Object[] key(Record it) {
        Object[] out = new Object[columns.size()];
        int i = 0;

        for (Column<?> c: columns) { out[i++] = it.getObject(c); }
        return out;
    }

    public <ValueT> ValueT key(Object[] key, Column<ValueT> col) {
        int i = columns.indexOf(col);
        if (i == -1) { throw new E("Invalid key column: %s", col.name); }
        return (ValueT)key[i];
    }

    public Long find(Object[] key, Tx tx) {
        Long out = tx.get(this, key);
        if (out == null) { out = records.get(key); } else if (out == -1L) { out = null; }
        return out;
    }

    public Stream<Map.Entry<Object[], Long>> findFirst(Object[] key, Tx tx) {
        Stream<Map.Entry<Object[], Long>> rs = records
                .subMap(key, true, records.lastKey(), true)
                .entrySet()
                .stream()
                .filter((i) -> !tx.isRemoved(this, i.getKey()));

        Stream<Map.Entry<Object[], Long>> txrs = tx.findFirst(this, key);
        return Stream.concat(rs, txrs).sorted((x, y) -> compareKeys(x.getKey(), y.getKey())).distinct();
    }

    @Override
    public void init(Record it, Column<?>...cols) {
        for (Column<?> c: (cols.length == 0) ? columns.toArray(cols) : cols) {
            if (!it.contains(c)) { it.setObject(c, c.initObject()); }
        }
    }

    public void commit(Object[] key, long recordId) {
        synchronized(file) {
            Encoding.writeTime(Instant.now(), file);

            int i = 0;
            for (Column<?> c : columns) { c.store(key[i++], file); }

            Encoding.writeLong(recordId, file);
        }

        if (recordId == -1L) { records.remove(key); } else { records.put(key, recordId); }
    }

    public void add(Record it, long id, Tx tx) {
        Object[] k = key(it);
        Long txr = tx.get(this, k);
        if ((records.containsKey(k) && (txr == null || txr != -1L)) || (txr != null && txr != -1L)) { throw new E("Duplicate key in index '%s'", name); }
        tx.put(this, k, id);
    }

    public boolean remove(Record it, Tx tx) { return tx.remove(this, key(it)); }

    public Stream<Map.Entry<Object[], Long>> records(Tx tx) {
        Stream<Map.Entry<Object[], Long>> rs = records
                .entrySet()
                .stream()
                .filter((i) -> !tx.isRemoved(this, i.getKey()));

        return Stream.concat(rs, tx.records(this)).distinct();
    }

    private SeekableByteChannel file;
    private final List<Column<?>> columns = new ArrayList<>();
    private final ConcurrentSkipListMap<Object[], Long> records = new ConcurrentSkipListMap<>(this::compareKeys);
}
