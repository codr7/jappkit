package codr7.jappkit.db;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Record {
    public static final Record DELETED = new Record();

    public Object getObject(Column<?> it) { return fields.get(it); }
    public <ValueT> ValueT get(Column<ValueT> it) {
        return (ValueT) getObject(it);
    }

    public void setObject(Column<?> column, Object value) {
        fields.put(column, value);
    }
    public <ValueT> void set(Column<ValueT> column, ValueT value) {
        setObject(column, value);
    }

    public Stream<Map.Entry<Column<?>, Object>> fields() {
        return fields.entrySet().stream();
    }

    private final Map<Column<?>, Object> fields = new TreeMap<>(Comparator.comparing(Object::toString));
}
