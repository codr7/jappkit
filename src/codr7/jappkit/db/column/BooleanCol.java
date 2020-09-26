package codr7.jappkit.db.column;

import codr7.jappkit.db.Col;
import codr7.jappkit.db.Table;
import codr7.jappkit.type.BooleanType;

public class BooleanCol extends Col<Boolean> {
    public BooleanCol(Table table, String name) {
        super(table, BooleanType.it, name);
    }
}
