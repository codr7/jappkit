package codr7.jappkit.db.columns;

import codr7.jappkit.db.*;
import codr7.jappkit.types.LongType;

public class LongCol extends Col<Long> {
    public LongCol(Table table, String name) {
        super(table, LongType.it, name);
    }
}
