package codr7.jappkit.db.columns;

import codr7.jappkit.Type;
import codr7.jappkit.db.*;
import codr7.jappkit.types.StringType;

public class StringColumn extends Column<String> {
    public static final Type<String> type = new StringType();

    public StringColumn(Table table, String name) {
        super(table, type, name);
    }

}
