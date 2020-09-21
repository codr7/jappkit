package codr7.jappkit.db.columns;

import codr7.jappkit.Type;
import codr7.jappkit.db.*;
import codr7.jappkit.types.LongType;

public class LongColumn extends Column<Long> {
    public static final Type<Long> type = new LongType();

    public LongColumn(Table table, String name) {
        super(table, type, name);
    }
}
