package codr7.jappkit.db;

import java.util.Map;
import java.util.TreeMap;

public class Record {
    public <ValueT> ValueT get(Column<ValueT> column) {
        return (ValueT) fields.get(column);
    }

    public <ValueT> void set(Column<ValueT> column, ValueT value) {
        fields.put(column, value);
    }

    private final Map<Column, Object> fields = new TreeMap<>();
}
