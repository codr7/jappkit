package codr7.jappkit.db;

import codr7.jappkit.db.errors.EIO;

import java.io.EOFException;
import java.io.IOException;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentSkipListMap;
import java.nio.file.Path;

public class Index extends Relation {
    public Index(Schema schema, String name) {
        super(schema, name);
    }

    @Override
    public void addColumn(Column<?> it) {
        columns.add(it);
    }

    @Override
    public void open(Instant maxTime) {
        try {
            Path dataPath = Path.of(schema.root.toString(), name + ".idx");
            file = Files.newByteChannel(dataPath, StandardOpenOption.CREATE, StandardOpenOption.READ, StandardOpenOption.WRITE);
        } catch (IOException e) {
            throw new EIO(e);
        }

        try {
            for (; ; ) {
                Object[] key = new Object[columns.size()];
                for (int i = 0; i < key.length; i++) { columns.get(i).load(file); }
                long recordId = Encoding.readLong(file);
                records.put(key, recordId);
            }
        } catch (EIO e) {
            if (e.getCause().getClass() != EOFException.class) {
                throw e;
            }
        }
    }

    @Override
    public void close() {
        try { file.close(); } catch (IOException e) { throw new EIO(e); }
        file = null;
    }

    public int compareKeys(Object[] x, Object[] y) {
        int i = 0;

        for (Column<?> c: columns) {
            if (x.length == i && y.length != i) {
                return Cmp.LT.asInt;
            }

            if (y.length == i) {
                return Cmp.GT.asInt;
            }
        }

        return Cmp.EQ.asInt;
    }

    private SeekableByteChannel file;
    private final List<Column<?>> columns = new ArrayList<>();
    private final Map<Object[], Long> records = new ConcurrentSkipListMap<>((Object[] x, Object[] y) -> compareKeys(x, y));
}
