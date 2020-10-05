package codr7.jappkit.db;

public class Ref<T> implements Comparable<Ref<T>> {
    public final Table refTable;
    public final long id;
    public final Mod.Make<T> make;

    public Ref(Table refTable, long id, Mod.Make<T> make) {
        this.refTable = refTable;
        this.id = id;
        this.make = make;
    }

    @Override
    public int compareTo(Ref<T> other) {
        var rt = refTable.compareTo(other.refTable);
        if (rt != 0) { return rt; }
        return Long.valueOf(id).compareTo(other.id);
    }

    public T deref(Tx tx) {
        if (cache == null && id != -1) { cache = make.call(refTable.load(id, tx)); }
        return cache;
    }

    public T clear() {
        T out = cache;
        cache = null;
        return out;
    }

    private T cache = null;
}