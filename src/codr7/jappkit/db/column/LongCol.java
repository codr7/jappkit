package codr7.jappkit.db.column;

import codr7.jappkit.db.*;
import codr7.jappkit.type.LongType;

public class LongCol extends Col<Long> {
    public LongCol(Table table, String name) {
        super(table, LongType.it, name);
    }
}
