package codr7.jappkit.db;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;

public class Tx {
    public Record set(Table table, long recordId) {
        Map<Long, Record> rs = updates.get(table);

        if (rs == null) {
            rs = new TreeMap<>();
            updates.put(table, rs);
        }

        Record r = rs.get(recordId);

        if (r == null || r == Record.DELETED) {
            r = new Record();
            rs.put(recordId, r);
        }

        return r;
    }

    public Record get(Table table, long recordId) {
        Map<Long, Record> rs = updates.get(table);
        if (rs == null) { return null; }
        return rs.get(recordId);
    }

    public void commit() {
        for (Map.Entry<Table, Map<Long, Record>> i: updates.entrySet()) {
            for (Map.Entry<Long, Record> j: i.getValue().entrySet()) {
                i.getKey().commit(j.getValue());
            }
        }

        updates.clear();
    }

    private Map<Table, Map<Long, Record>> updates = new TreeMap<>(Comparator.comparing(Object::toString));
}
