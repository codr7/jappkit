package codr7.jappkit.db;

import codr7.jappkit.db.columns.LongColumn;
import codr7.jappkit.db.errors.IOError;

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
    public void addColumn(Column<?> it) {
        columns.put(it.name, it);
    }

    @Override
    public void open(Instant maxTime) {
        try {
            Path keyPath = Path.of(schema.root.toString(), name + ".key");
            keyFile = Files.newByteChannel(keyPath, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
            Path dataPath = Path.of(schema.root.toString(), name + ".dat");
            dataFile = Files.newByteChannel(dataPath, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new IOError(e);
        }

        try {
            for (; ; ) {
                long recordId = Encoding.readLong(keyFile);
                long pos = Encoding.readLong(keyFile);
                records.put(recordId, pos);
            }
        } catch (IOError e) {
            if (e.cause.getClass() != EOFException.class) {
                throw e;
            }
        }
    }

    @Override
    public void close() {
        keyFile = null;
        dataFile = null;
    }

    public void commit(Record it) {
        Long id = it.get(Table.this.id);
        if (id == null) { throw new E(String.format("Missing id for table: %s", name)); }
        long pos = -1;

        try {
            pos = dataFile.size();
            dataFile.position(pos);
        } catch (IOException e) { throw new IOError(e); }

        it.write(dataFile);
        Encoding.writeLong(id, keyFile);
        Encoding.writeLong(pos, keyFile);
        records.put(id, pos);
    }

    public long getNextRecordId() {
        return nextRecordId.incrementAndGet();
    }

    public Record load(long recordId, Tx tx) {
        Record r = tx.get(this, recordId);

        if (r == null) {
            Long pos = records.get(recordId);
            if (pos == null) { return null; }
            try { dataFile.position(pos); }
            catch (IOException e) { throw new IOError(e); }

            r = new Record();
            long len = Encoding.readLong(dataFile);

            for (long i = 0; i < len; i++) {
              String cn = Encoding.readString(dataFile);
              Column<?> c = columns.get(cn);
              if (c == null) { throw new E(String.format("Unknown column: %s", cn)); }
              r.setObject(c, c.load(dataFile));
            }
        }

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

        final long idv = id.longValue();
        final Record txr = tx.set(this, idv);

        it.fields().forEach((Map.Entry<Column<?>, Object> f) -> {
            txr.setObject(f.getKey(), f.getValue());
        });
    }

    public Stream<Record> records(Tx tx) {
        Stream<Record> rs = records.keySet().stream().map((Long id) -> load(id, tx));
        Stream<Record> txrs = tx.records(this).filter((r) -> records.get(r.get(id)) == null);
        return Stream.concat(rs, txrs);
    }

    private SeekableByteChannel keyFile;
    private SeekableByteChannel dataFile;
    private final Map<String, Column<?>> columns = new HashMap<>();
    private final List<Index> indexes = new ArrayList<>();
    private final Map<Long, Long> records = new ConcurrentSkipListMap<>();
    private final AtomicLong nextRecordId = new AtomicLong(0);
}