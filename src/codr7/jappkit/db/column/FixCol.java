package codr7.jappkit.db.column;

import codr7.jappkit.db.Col;
import codr7.jappkit.db.Table;
import codr7.jappkit.type.FixType;

public class FixCol extends Col<Long> {
    public FixCol(Table table, String name) { super(table, FixType.it, name); }
}
