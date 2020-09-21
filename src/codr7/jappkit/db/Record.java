package codr7.jappkit.db;

import java.nio.channels.SeekableByteChannel;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Record {
    public static final Record DELETED = new Record();

    public Object getObject(Column<?> it) { return fields.get(it); }
    public <ValueT> ValueT get(Column<ValueT> it) {
        return it.get((ValueT) getObject(it));
    }

    public void setObject(Column<?> column, Object value) {
        fields.put(column, value);
    }

    public <ValueT> Record set(Column<ValueT> column, ValueT value) {
        setObject(column, column.set(value));
        return this;
    }

    public Stream<Map.Entry<Column<?>, Object>> fields() {
        return fields.entrySet().stream();
    }

    public void write(SeekableByteChannel out) {
        int len = fields.size();
        Encoding.writeLong(len, out);

        for (Map.Entry<Column<?>, Object> f: fields.entrySet()) {
            Column<?> c = f.getKey();
            Encoding.writeString(c.name, out);
            c.store(f.getValue(), out);
        }
    }

    private final Map<Column<?>, Object> fields = new TreeMap<>(Comparator.comparing(Object::toString));
}
