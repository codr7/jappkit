package codr7.jappkit.db.columns;

import codr7.jappkit.db.Cmp;
import codr7.jappkit.db.Column;

public class LongColumn extends Column<Long> {
    public LongColumn(String name) {
        super(name);
    }

    @Override
    public Cmp cmp(Long x, Long y) {
        return Cmp.valueOf(x.compareTo(y));
    }
}
