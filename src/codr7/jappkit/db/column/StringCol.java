package codr7.jappkit.db.column;

import codr7.jappkit.db.*;
import codr7.jappkit.type.StringType;

public class StringCol extends Col<String> {
    public StringCol(Table table, String name) {
        super(table, StringType.it, name);
    }
}
