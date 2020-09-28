package codr7.jappkit.db.column;

import codr7.jappkit.db.Col;
import codr7.jappkit.db.Mod;
import codr7.jappkit.db.Ref;
import codr7.jappkit.db.Table;
import codr7.jappkit.db.type.RefType;

public class RefCol <T extends Mod> extends Col<Ref<T>> {
    public final Table refTable;
    public final Mod.Make<T> make;

    public RefCol(Table table, String name, Table refTable, Mod.Make<T> make) {
        super(table, new RefType(refTable, make), name);
        this.refTable = refTable;
        this.make = make;
    }

    public Ref<T> ref(T it) { return new Ref<T>(refTable, it.id, make); }
}