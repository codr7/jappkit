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
                long recordId = Encoding.readLong(keyFile);
                long pos = Encoding.readLong(keyFile);
                records.put(recordId, pos);
            }
        } catch (EIO e) {
            if (e.getCause().getClass() != EOFException.class) {
                throw e;
            }
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

    public void commit(Record it) {
        Long id = it.get(Table.this.id);
        if (id == null) { throw new E("Missing id for table: %s", name); }
        long pos = -1;

        synchronized(dataFile) {
            try {
                pos = dataFile.size();
                dataFile.position(pos);
            } catch (IOException e) {
                throw new EIO(e);
            }

            it.write(dataFile);
        }

        synchronized(keyFile) {
            Encoding.writeLong(id, keyFile);
            Encoding.writeLong(pos, keyFile);
        }

        records.put(id, pos);
    }

    public long getNextRecordId() {
        return nextRecordId.incrementAndGet();
    }

    public Record load(long recordId) {
        Long pos = records.get(recordId);
        if (pos == null) { return null; }
        final Record r = new Record();

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
        Record r = tx.get(this, recordId);
        if (r == null) { return load(recordId); }
        final Record lr = new Record();

        r.fields().forEach((Map.Entry<Column<?>, Object> f) -> {
            lr.setObject(f.getKey(), f.getValue());
        });

        return lr;
    }

    public void store(Record it, Tx tx) {
        Long id = it.get(Table.this.id);

        if (id == null) {
            id = getNextRecordId();
            it.set(Table.this.id, id);
        }

        Record pr = load(id);

        if (pr != null) {
            for (Index idx: indexes) { idx.remove(pr, tx); }
        }

        final long idv = id.longValue();
        final Record txr = tx.set(this, idv);

        it.fields().forEach((Map.Entry<Column<?>, Object> f) -> { txr.setObject(f.getKey(), f.getValue()); });
        for (Index idx: indexes) { idx.add(it, id, tx); }
    }

    public boolean delete(long recordId, Tx tx) {
        if (tx.delete(this, recordId)) { return true; }
        return records.remove(recordId) != null;
    }

    public Stream<Record> records(Tx tx) {
        Stream<Record> rs = records
                .keySet()
                .stream()
                .filter((id) -> !tx.isDeleted(this, id))
                .map((id) -> load(id, tx));

        return Stream.concat(rs, tx.records(this)).distinct();
    }

    private SeekableByteChannel keyFile;
    private SeekableByteChannel dataFile;
    private final Map<String, Column<?>> columns = new HashMap<>();
    private final List<Index> indexes = new ArrayList<>();
    private final Map<Long, Long> records = new ConcurrentSkipListMap<>();
    private final AtomicLong nextRecordId = new AtomicLong(0);
}