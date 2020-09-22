package codr7.jappkit.db;

import codr7.jappkit.E;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Tx {
    public final Tx parent;

    public Tx(Tx parent) { this.parent = parent; }
    public Tx() { this(null); }

    public ConstRecord get(Table table, long recordId) {
        Map<Long, ConstRecord> rs = tableUpdates.get(table);
        if (rs == null) { return null; }
        return rs.get(recordId);
    }

    public Stream<Map.Entry<Long, ConstRecord>> records(Table table) {
        Map<Long, ConstRecord> rs = tableUpdates.get(table);
        if (rs == null) { return Stream.empty(); }
        return rs.entrySet().stream().filter((i) -> i.getValue() != Record.DELETED);
    }

    public Record set(Table table, long recordId) {
        Map<Long, ConstRecord> rs = tableUpdates.get(table);

        if (rs == null) {
            rs = new TreeMap<>();
            tableUpdates.put(table, rs);
        }

        Record r = (Record)rs.get(recordId);

        if (r == null || r == Record.DELETED) {
            r = new Record();
            rs.put(recordId, r);
        }

        return r;
    }

    public boolean delete(Table table, long recordId) {
        Map<Long, ConstRecord> rs = tableUpdates.get(table);
        if (rs == null) { return false; }
        ConstRecord pr = rs.put(recordId, Record.DELETED);
        return pr != null && pr != Record.DELETED;
    }

    public boolean isDeleted(Table tbl, long recordId) {
        Map<Long, ConstRecord> rs = tableUpdates.get(tbl);
        if (rs == null) { return false; }
        return rs.get(recordId) == Record.DELETED;
    }

    public Long get(Index index, Object[] key) {
        Map<Object[], Long> rs = indexUpdates.get(index);
        if (rs == null) { return null; }
        return rs.get(key);
    }

    public Stream<Map.Entry<Object[], Long>> records(Index idx) {
        Map<Object[], Long> rs = indexUpdates.get(idx);
        if (rs == null) { return Stream.empty(); }
        return rs.entrySet().stream().filter((i) -> i.getValue() != -1L);
    }

    public void put(final Index index, Object[]key, long recordId) {
        TreeMap<Object[], Long> rs = indexUpdates.get(index);

        if (rs == null) {
            rs = new TreeMap<>(index::compareKeys);
            indexUpdates.put(index, rs);
        }

        Long pid = rs.get(key);
        if (pid != null && pid != -1L) { throw new E("Duplicate key in index '%s'", index.name); }
        rs.put(key, recordId);
    }

    public boolean remove(Index idx, Object[] key) {
        TreeMap<Object[], Long> rs = indexUpdates.get(idx);

        if (rs == null) {
            rs = new TreeMap<>(idx::compareKeys);
            indexUpdates.put(idx, rs);
        }

        Long pid = rs.put(key, -1L);
        return pid != null && pid != -1L;
    }

    public boolean isRemoved(Index idx, Object[] key) {
        TreeMap<Object[], Long> rs = indexUpdates.get(idx);
        if (rs == null) { return false; }
        Long id = rs.get(key);
        return id != null && id == -1L;
    }

    public Stream<Map.Entry<Object[], Long>> findFirst(Index idx, Object[] key) {
        TreeMap<Object[], Long> rs = indexUpdates.get(idx);
        if (rs == null) { return Stream.empty(); }

        return rs
                .subMap(key, true, rs.lastKey(), true)
                .entrySet()
                .stream()
                .filter((i) -> i.getValue() != -1L);
    }

    public void commit() {
        if (parent == null) {
            for (Map.Entry<Table, Map<Long, ConstRecord>> i : tableUpdates.entrySet()) {
                for (Map.Entry<Long, ConstRecord> j : i.getValue().entrySet()) {
                    i.getKey().commit(j.getValue(), j.getKey());
                }
            }


            for (Map.Entry<Index, TreeMap<Object[], Long>> i : indexUpdates.entrySet()) {
                for (Map.Entry<Object[], Long> j : i.getValue().entrySet()) {
                    i.getKey().commit(j.getKey(), j.getValue());
                }
            }
        } else {
            parent.tableUpdates.putAll(tableUpdates);
            parent.indexUpdates.putAll(indexUpdates);
        }

        rollback();
    }

    public void rollback() {
        tableUpdates.clear();
        indexUpdates.clear();
    }

    private Map<Index, TreeMap<Object[], Long>> indexUpdates = new TreeMap<>();
    private Map<Table, Map<Long, ConstRecord>> tableUpdates = new TreeMap<>();
}
