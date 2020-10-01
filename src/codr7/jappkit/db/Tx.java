package codr7.jappkit.db;

import codr7.jappkit.E;

import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Tx {
    public final Tx parent;

    public Tx(Tx parent) { this.parent = parent; }
    public Tx() { this(null); }

    public ConstRec get(Table table, long recId) {
        Map<Long, ConstRec> rs = tableUpdates.get(table);
        if (rs == null) { return null; }
        return rs.get(recId);
    }

    public Stream<Map.Entry<Long, ConstRec>> records(Table table) {
        Map<Long, ConstRec> rs = tableUpdates.get(table);
        if (rs == null) { return Stream.empty(); }
        return rs.entrySet().stream().filter((i) -> i.getValue() != Rec.DELETED);
    }

    public Rec set(Table table, long recId) {
        Map<Long, ConstRec> rs = tableUpdates.get(table);

        if (rs == null) {
            rs = new TreeMap<>();
            tableUpdates.put(table, rs);
        }

        Rec r = (Rec)rs.get(recId);

        if (r == null || r == Rec.DELETED) {
            r = new Rec();
            rs.put(recId, r);
        }

        return r;
    }

    public boolean delete(Table table, long recId) {
        Map<Long, ConstRec> rs = tableUpdates.get(table);
        if (rs == null) { return false; }
        ConstRec pr = rs.put(recId, Rec.DELETED);
        return pr != null && pr != Rec.DELETED;
    }

    public boolean isDeleted(Table tbl, long recId) {
        Map<Long, ConstRec> rs = tableUpdates.get(tbl);
        if (rs == null) { return false; }
        return rs.get(recId) == Rec.DELETED;
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

    public void put(final Index index, Object[]key, long recId) {
        TreeMap<Object[], Long> rs = indexUpdates.get(index);

        if (rs == null) {
            rs = new TreeMap<>(index::compareKeys);
            indexUpdates.put(index, rs);
        }

        Long pid = rs.get(key);
        if (pid != null && pid != -1L) { throw new E("Duplicate key in index '%s'", index.name); }
        rs.put(key, recId);
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
        if (rs == null || rs.isEmpty() || idx.compareKeys(key, rs.lastKey()) > 0) { return Stream.empty(); }
        if (idx.compareKeys(key, rs.firstKey()) < 0) { key = rs.firstKey(); }

        return rs
                .subMap(key, true, rs.lastKey(), true)
                .entrySet()
                .stream()
                .filter((i) -> i.getValue() != -1L);
    }

    public void commit() {
        if (parent == null) {
            for (Map.Entry<Table, Map<Long, ConstRec>> i : tableUpdates.entrySet()) {
                for (Map.Entry<Long, ConstRec> j : i.getValue().entrySet()) {
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
    private Map<Table, Map<Long, ConstRec>> tableUpdates = new TreeMap<>();
}
