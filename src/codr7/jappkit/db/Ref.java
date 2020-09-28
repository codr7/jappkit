package codr7.jappkit.db;

public class Ref<T> {
    public final Table refTable;
    public final long id;
    public final Mod.Make<T> make;

    public Ref(Table refTable, long id, Mod.Make<T> make) {
        this.refTable = refTable;
        this.id = id;
        this.make = make;
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