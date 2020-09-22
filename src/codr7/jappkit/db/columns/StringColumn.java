package codr7.jappkit.db.columns;

import codr7.jappkit.db.*;
import codr7.jappkit.types.StringType;

public class StringColumn extends Column<String> {
    public StringColumn(Table table, String name) {
        super(table, StringType.it, name);
    }
}
