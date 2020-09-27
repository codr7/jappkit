package codr7.jappkit.db.column;

import codr7.jappkit.db.Col;
import codr7.jappkit.db.RecProxy;
import codr7.jappkit.db.Table;
import codr7.jappkit.db.type.RecType;


public class RecCol extends Col<RecProxy> {
    public final Table refTable;

    public RecCol(Table table, String name, Table refTable) {
        super(table, new RecType(refTable), name);
        this.refTable = refTable;
    }
}