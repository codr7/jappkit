package codr7.jappkit.db.columns;

import codr7.jappkit.db.Col;
import codr7.jappkit.db.Table;
import codr7.jappkit.types.BooleanType;

public class BooleanCol extends Col<Boolean> {
    public BooleanCol(Table table, String name) {
        super(table, BooleanType.it, name);
    }
}
