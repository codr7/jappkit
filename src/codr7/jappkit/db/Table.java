package codr7.jappkit.db;

import codr7.jappkit.db.columns.LongColumn;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

public class Table extends Relation {
    public final Column<Long> id;

    public Table(Schema schema, String name) {
        super(schema, name);
        id = new LongColumn(this, "id");
        System.err.println(columns);
    }

    @Override
    public void addColumn(Column<?> it) {
        columns.put(it.name, it);
    }

    @Override
    public void open(Instant maxTime) {
        keyFile = new File(Path.of(schema.root.toString(), name, ".key").toString());
        dataFile = new File(Path.of(schema.root.toString(), name, ".dat").toString());
        //todo
    }

    @Override
    public void close() {
        keyFile = null;
        dataFile = null;
    }

    public long getNextRecordId() {
        return nextRecordId.incrementAndGet();
    }

    public Record load(long recordId, Tx tx) {
        Record r = null;
        Record txr = tx.get(this, recordId);

        if (txr != null) {
            final Record lr = new Record();

            txr.fields().forEach((Map.Entry<Column<?>, Object> f) -> {
                lr.setObject(f.getKey(), f.getValue());
            });

            r = lr;
        }

        return r;
    }

    public void store(Record it, Tx tx) {
        Long id = it.get(Table.this.id);

        if (id == null) {
            id = getNextRecordId();
            it.set(Table.this.id, id);
        }

        final long idv = id.longValue();

        it.fields().forEach((Map.Entry<Column<?>, Object> f) -> {
            tx.set(idv, f.getKey(), f.getValue());
        });
    }

    private File keyFile;
    private File dataFile;
    private final Map<String, Column<?>> columns = new HashMap<>();
    private final List<Index> indexes = new ArrayList<>();
    private final Map<Long, Long> records = new ConcurrentSkipListMap<>();
    private final AtomicLong nextRecordId = new AtomicLong(0);
}