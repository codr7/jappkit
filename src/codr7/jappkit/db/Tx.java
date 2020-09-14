package codr7.jappkit.db;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Tx {
    public void set(long recordId, Column<?> column, Object value) {
        Map<Long, Record> rs = updates.get(column.table);

        if (rs == null) {
            rs = new TreeMap<>();
            updates.put(column.table, rs);
        }

        Record r = rs.get(recordId);

        if (r == null || r == Record.DELETED) {
            r = new Record();
            rs.put(recordId, r);
        }

        r.setObject(column, value);
    }

    public Record get(Table table, long recordId) {
        Map<Long, Record> rs = updates.get(table);
        if (rs == null) { return null; }
        return rs.get(recordId);
    }

    private Map<Table, Map<Long, Record>> updates = new TreeMap<>(Comparator.comparing(Object::toString));
}
