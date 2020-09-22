package codr7.jappkit.db.columns;

import codr7.jappkit.db.*;
import codr7.jappkit.types.StringType;

public class StringCol extends Col<String> {
    public StringCol(Table table, String name) {
        super(table, StringType.it, name);
    }
}
