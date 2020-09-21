package codr7.jappkit.db.columns;

import codr7.jappkit.Type;
import codr7.jappkit.db.*;
import codr7.jappkit.types.TimeType;

import java.time.Instant;

public class TimeColumn extends Column<Instant> {
    public static final Type<Instant> type = new TimeType();

    public TimeColumn(Table table, String name) {
        super(table, type, name);
    }
}