package codr7.jappkit.db;

public class Prec {
    public Table refTable;
    public final long id;

    public Prec(Table refTable, long id) {
        this.refTable = refTable;
        this.id = id;
    }

    public Rec rec(Tx tx) {
        if (rec == null && id != -1) { rec = refTable.load(id, tx); }
        return rec;
    }

    public Rec clear() {
        Rec out = rec;
        rec = null;
        return out;
    }

    private Rec rec = null;
}
