package codr7.jappkit.db;

import java.io.File;
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
        file = new File(Path.of(schema.root, name, ".idx").toString());
    }

    @Override
    public void close() {
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

    private File file;
    private final List<Column<?>> columns = new ArrayList<>();
    private final Map<Object[], Long> records = new ConcurrentSkipListMap<>((Object[] x, Object[] y) -> compareKeys(x, y));
}
