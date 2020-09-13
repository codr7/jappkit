package codr7.jappkit;

import java.io.File;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.nio.file.Path;

public class Index extends Definition{
    public Index(Schema schema, String name) {
        super(schema, name);
    }

    @Override
    public void open(Instant maxTime) {
        file = new File(Path.of(schema.root, name, ".idx").toString());
    }

    @Override
    public void close() {
        file = null;
    }

    private File file;
    private final Map<Long, Long> records = new ConcurrentSkipListMap<>();
}
