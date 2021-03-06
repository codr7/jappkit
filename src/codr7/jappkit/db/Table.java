package codr7.jappkit.db;

import codr7.jappkit.E;
import codr7.jappkit.db.column.LongCol;
import codr7.jappkit.error.EOF;

import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Table extends Relation {
    public final Col<Long> id;

    public Table(Schema schema, String name) {
        super(schema, name);
        id = new LongCol(this, "id");
        schema.addTable(this);
    }

    @Override
    public Table addCol(Col<?> it) {
        if (cols.containsKey(it.name)) { throw new E("Duplicate col: %v", it.name); }
        cols.put(it.name, it);
        return this;
    }

    public Col<?> findCol(String name) { return cols.get(name); }

    @Override
    public Stream<Col<?>> cols() { return cols.values().stream().filter((c) -> c != id); }

    public Table addIndex(Index it) {
        indexes.add(it);
        return this;
    }

    @Override
    public void open(Instant maxTime) {
        try {
            var keyPath = Path.of(schema.root.toString(), name + ".key");
            keyFile = Files.newByteChannel(keyPath, fileOptions);
            var dataPath = Path.of(schema.root.toString(), name + ".dat");
            dataFile = Files.newByteChannel(dataPath, fileOptions);
        } catch (IOException e) { throw new E(e); }

        try {
            for (; ; ) {
                var ts = Encoding.readTime(keyFile);
                if (ts.compareTo(maxTime) > 0) { break; }
                var recordId = Encoding.readLong(keyFile);
                var pos = Encoding.readLong(keyFile);
                if (pos == -1) { recs.remove(recordId); } else { recs.put(recordId, pos); }
            }
        } catch (EOF e) { }
    }

    @Override
    public void close() {
        try {
            keyFile.close();
            keyFile = null;

            dataFile.close();
            dataFile = null;
        } catch (IOException e) { throw new E(e); }

        recs.clear();
        nextRecId.set(0L);
    }

    public void commit(ConstRec it, long recordId) {
        long pos = -1;

        if (it != Rec.DELETED) {
            synchronized (dataFile) {
                try {
                    pos = dataFile.size();
                    dataFile.position(pos);
                } catch (IOException e) { throw new E(e); }

                it.write(dataFile);
            }
        }

        synchronized(keyFile) {
            Encoding.writeTime(Instant.now(), keyFile);
            Encoding.writeLong(recordId, keyFile);
            Encoding.writeLong((it == Rec.DELETED) ? -1L : pos, keyFile);
        }

        recs.put(recordId, pos);
    }

    public long getNextRecId() { return nextRecId.incrementAndGet(); }

    @Override
    public void init(Rec it, Col<?>...cols) {
        var cs = (cols.length == 0) ? cols() : Arrays.stream(cols);

        cs.forEach((c) -> {
            if (!it.contains(c)) { it.setObject(c, c.initObject()); }
        });
    }

    public Rec load(long recId) {
        var pos = recs.get(recId);
        if (pos == null) { return null; }
        final var r = new Rec();
        r.set(id, recId);

        synchronized(dataFile) {
            try { dataFile.position(pos); } catch (IOException e) { throw new E(e); }
            r.load(dataFile, this);
        }

        return r;
    }

    public Rec load(long recId, Tx tx) {
        var r = tx.get(this, recId);
        if (r == null && (r = load(recId)) == null) { return null; }
        final Rec lr = new Rec();
        lr.set(id, recId);
        r.fields().forEach((f) -> { lr.setObject(f.getKey(), f.getValue()); });
        return lr;
    }

    public void store(Rec it, Tx tx) {
        var id = it.get(Table.this.id);

        if (id == null) {
            id = getNextRecId();
            it.set(Table.this.id, id);
        } else {
            var pr = load(id, tx);

            if (pr != null) {
                for (var idx: indexes) { idx.remove(pr, tx); }
            }
        }

        final var txr = tx.set(this, id);

        it.fields().forEach((f) -> {
            var c = f.getKey();
            txr.setObject(c, f.getValue());
        });

        for (var idx: indexes) { idx.add(it, id, tx); }
    }

    public boolean delete(long recordId, Tx tx) {
        return tx.delete(this, recordId);
    }

    public Stream<Rec> recs(Tx tx) {
        var rs = recs
                .keySet()
                .stream()
                .filter((id) -> !tx.isDeleted(this, id))
                .map((id) -> load(id, tx));

        var txrs = tx
                .records(this)
                .map((i) -> i.getValue().clone().set(id, i.getKey()));

        return Stream.concat(rs, txrs).distinct();
    }

    private SeekableByteChannel keyFile;
    private SeekableByteChannel dataFile;
    private final Map<String, Col<?>> cols = new HashMap<>();
    private final List<Index> indexes = new ArrayList<>();
    private final Map<Long, Long> recs = new ConcurrentSkipListMap<>();
    private final AtomicLong nextRecId = new AtomicLong(0);
}