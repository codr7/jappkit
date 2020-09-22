package codr7.jappkit.db.columns;

import codr7.jappkit.db.*;
import codr7.jappkit.types.TimeType;

import java.time.Instant;

public class TimeCol extends Col<Instant> {
    public TimeCol(Table table, String name) {
        super(table, TimeType.it, name);
    }
}