package codr7.jappkit.db.columns;

import codr7.jappkit.db.Column;
import codr7.jappkit.db.Table;
import codr7.jappkit.types.BooleanType;

public class BooleanColumn extends Column<Boolean> {
    public BooleanColumn(Table table, String name) {
        super(table, BooleanType.it, name);
    }
}
