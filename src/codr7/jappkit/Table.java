package codr7.jappkit;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.util.concurrent.atomic.AtomicLong;

public class Table extends Definition {
    public final Column<Long> id = new Column<Long>("id");

    public Table(Schema schema, String name) {
        super(schema, name);
    }

    @Override
    public void open(Instant maxTime) {
        keyFile = new File(Path.of(schema.root, name, ".key").toString());
        dataFile = new File(Path.of(schema.root, name, ".dat").toString())
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

    private File keyFile;
    private File dataFile;
    private final Map<String, Column<?>> columns = new HashMap<>();
    private final List<Index> indexes = new ArrayList<>();
    private final Map<Long, Long> records = new ConcurrentSkipListMap<>();
    private final AtomicLong nextRecordId = new AtomicLong(0);
}