package codr7.jappkit.db.columns;

import codr7.jappkit.Type;
import codr7.jappkit.db.*;
import codr7.jappkit.types.TimeType;

import java.time.Instant;

public class TimeColumn extends Column<Instant> {
    public TimeColumn(Table table, String name) {
        super(table, TimeType.it, name);
    }
}