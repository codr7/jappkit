package codr7.jappkit.db.columns;

import codr7.jappkit.db.Cmp;
import codr7.jappkit.db.Column;
import codr7.jappkit.db.Table;

public class StringColumn extends Column<String> {
    public StringColumn(Table table, String name) {
        super(table, name);
    }

    @Override
    public Cmp cmp(String x, String y) {
        return Cmp.valueOf(x.compareTo(y));
    }
}
