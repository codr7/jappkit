package codr7.jappkit.db;

import codr7.jappkit.db.columns.LongColumn;
import codr7.jappkit.db.errors.EIO;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Stream;

public class Table extends Relation {
    public final Column<Long> id;

    public Table(Schema schema, String name) {
        super(schema, name);
        id = new LongColumn(this, "id");
        id.isVirtual = true;
        schema.addTable(this);
    }

    @Override
    public Table addColumn(Column<?> it) {
        if (columns.containsKey(it.name)) { throw new E("Duplicate column: %v", it.name); }
        columns.put(it.name, it);
        return this;
    }

    public Table addIndex(Index it) {
        indexes.add(it);
        return this;
    }

    @Override
    public void open(Instant maxTime) {
        try {
            Path keyPath = Path.of(schema.root.toString(), name + ".key");
            keyFile = Files.newByteChannel(keyPath, fileOptions);
            Path dataPath = Path.of(schema.root.toString(), name + ".dat");
            dataFile = Files.newByteChannel(dataPath, fileOptions);
        } catch (IOException e) {
            throw new EIO(e);
        }

        try {
            for (; ; ) {
                Instant ts = Encoding.readTime(keyFile);
                if (ts.compareTo(maxTime) > 0) { break; }
                long recordId = Encoding.readLong(keyFile);
                long pos = Encoding.readLong(keyFile);
                if (pos == -1) { records.remove(recordId); } else { records.put(recordId, pos); }
            }
        } catch (EIO e) {
            if (e.getCause().getClass() != EOFException.class) { throw e; }
        }
    }

    @Override
    public void close() {
        try {
            keyFile.close();
            keyFile = null;

            dataFile.close();
            dataFile = null;
        } catch (IOException e) { throw new EIO(e); }

        records.clear();
        nextRecordId.set(0L);
    }

    public void commit(ConstRecord it, long recordId) {
        long pos = -1;

        if (it != Record.DELETED) {
            synchronized (dataFile) {
                try {
                    pos = dataFile.size();
                    dataFile.position(pos);
                } catch (IOException e) { throw new EIO(e); }

                it.write(dataFile);
            }
        }

        synchronized(keyFile) {
            Encoding.writeTime(Instant.now(), keyFile);
            Encoding.writeLong(recordId, keyFile);
            Encoding.writeLong((it == Record.DELETED) ? -1L : pos, keyFile);
        }

        records.put(recordId, pos);
    }

    public long getNextRecordId() { return nextRecordId.incrementAndGet(); }

    @Override
    public void init(Record it, Column<?>...cols) {
        for (Column<?> c: (cols.length == 0) ? columns.values().toArray(cols) : cols) {
            if (c.isVirtual) { continue; }
            if (!it.contains(c)) { it.setObject(c, c.initObject()); }
        }
    }

    public Record load(long recordId) {
        Long pos = records.get(recordId);
        if (pos == null) { return null; }
        final Record r = new Record();
        r.set(id, recordId);

        synchronized(dataFile) {
            try { dataFile.position(pos); } catch (IOException e) { throw new EIO(e); }
            long len = Encoding.readLong(dataFile);

            for (long i = 0; i < len; i++) {
                String cn = Encoding.readString(dataFile);
                Column<?> c = columns.get(cn);
                if (c == null) { throw new E("Unknown column: %s", cn); }
                r.setObject(c, c.load(dataFile));
            }
        }

        return r;
    }

    public Record load(long recordId, Tx tx) {
        ConstRecord r = tx.get(this, recordId);
        if (r == null) { return load(recordId); }
        final Record lr = new Record();
        lr.set(id, recordId);
        r.fields().forEach((Map.Entry<Column<?>, Object> f) -> { lr.setObject(f.getKey(), f.getValue()); });
        return lr;
    }

    public void store(Record it, Tx tx) {
        Long id = it.get(Table.this.id);

        if (id == null) {
            id = getNextRecordId();
            it.set(Table.this.id, id);
        } else {
            Record pr = load(id, tx);

            if (pr != null) {
                for (Index idx: indexes) { idx.remove(pr, tx); }
            }
        }

        final Record txr = tx.set(this, id);

        it.fields().forEach((Map.Entry<Column<?>, Object> f) -> {
            Column<?> c = f.getKey();
            if (!c.isVirtual) { txr.setObject(c, f.getValue()); }
        });

        for (Index idx: indexes) { idx.add(it, id, tx); }
    }

    public boolean delete(long recordId, Tx tx) {
        return tx.delete(this, recordId);
    }

    public Stream<Record> records(Tx tx) {
        Stream<Record> rs = records
                .keySet()
                .stream()
                .filter((id) -> !tx.isDeleted(this, id))
                .map((id) -> load(id, tx));

        Stream<Record> txrs = tx
                .records(this)
                .map((i) -> i.getValue().clone().set(id, i.getKey()));

        return Stream.concat(rs, txrs).distinct();
    }

    private SeekableByteChannel keyFile;
    private SeekableByteChannel dataFile;
    private final Map<String, Column<?>> columns = new HashMap<>();
    private final List<Index> indexes = new ArrayList<>();
    private final Map<Long, Long> records = new ConcurrentSkipListMap<>();
    private final AtomicLong nextRecordId = new AtomicLong(0);
}