package codr7.jappkit.db.columns;

import codr7.jappkit.db.Cmp;
import codr7.jappkit.db.Column;

public class StringColumn extends Column<String> {
    public StringColumn(String name) {
        super(name);
    }

    @Override
    public Cmp cmp(String x, String y) {
        return Cmp.valueOf(x.compareTo(y));
    }
}
