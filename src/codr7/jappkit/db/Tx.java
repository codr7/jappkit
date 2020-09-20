package codr7.jappkit.db;

import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Tx {
    public Record get(Table table, long recordId) {
        Map<Long, Record> rs = tableUpdates.get(table);
        if (rs == null) { return null; }
        return rs.get(recordId);
    }

    public Stream<Record> records(Table table) {
        Map<Long, Record> rs = tableUpdates.get(table);
        if (rs == null) { return Stream.empty(); }
        return rs.values().stream();
    }

    public Record set(Table table, long recordId) {
        Map<Long, Record> rs = tableUpdates.get(table);

        if (rs == null) {
            rs = new TreeMap<>();
            tableUpdates.put(table, rs);
        }

        Record r = rs.get(recordId);

        if (r == null || r == Record.DELETED) {
            r = new Record();
            rs.put(recordId, r);
        }

        return r;
    }

    public boolean delete(Table table, long recordId) {
        Map<Long, Record> rs = tableUpdates.get(table);
        if (rs == null) { return false; }
        Record pr = rs.put(recordId, Record.DELETED);
        return pr != null && pr != Record.DELETED;
    }

    public boolean isDeleted(Table tbl, long recordId) {
        Map<Long, Record> rs = tableUpdates.get(tbl);
        if (rs == null) { return false; }
        return rs.get(recordId) == Record.DELETED;
    }

    public Long get(Index index, Object[] key) {
        Map<Object[], Long> rs = indexUpdates.get(index);
        if (rs == null) { return null; }
        return rs.get(key);
    }

    public void put(final Index index, Object[]key, long recordId) {
        TreeMap<Object[], Long> rs = indexUpdates.get(index);

        if (rs == null) {
            rs = new TreeMap<>(index::compareKeys);
            indexUpdates.put(index, rs);
        }

        if (rs.containsKey(key)) { throw new E("Duplicate key in index '%s'", index.name); }
        rs.put(key, recordId);
    }

    public boolean delete(Index idx, Object[] key) {
        TreeMap<Object[], Long> rs = indexUpdates.get(idx);
        if (rs == null) { return false; }
        Long pid = rs.put(key, -1L);
        return pid != null && pid != -1L;
    }

    public boolean isDeleted(Index idx, Object[] key) {
        TreeMap<Object[], Long> rs = indexUpdates.get(idx);
        if (rs == null) { return false; }
        return rs.get(key) == -1L;
    }

    public Stream<Map.Entry<Object[], Long>> findFirst(Index idx, Object[] key) {
        TreeMap<Object[], Long> rs = indexUpdates.get(idx);
        if (rs == null) { return Stream.empty(); }
        return rs.subMap(key, true, rs.lastKey(), true).entrySet().stream();
    }

    public void commit() {
        for (Map.Entry<Table, Map<Long, Record>> i: tableUpdates.entrySet()) {
            for (Map.Entry<Long, Record> j: i.getValue().entrySet()) { i.getKey().commit(j.getValue()); }
        }

        tableUpdates.clear();

        for (Map.Entry<Index, TreeMap<Object[], Long>> i: indexUpdates.entrySet()) {
            for (Map.Entry<Object[], Long> j: i.getValue().entrySet()) { i.getKey().commit(j.getKey(), j.getValue()); }
        }

        indexUpdates.clear();
    }

    private Map<Index, TreeMap<Object[], Long>> indexUpdates = new TreeMap<>();
    private Map<Table, Map<Long, Record>> tableUpdates = new TreeMap<>();
}
