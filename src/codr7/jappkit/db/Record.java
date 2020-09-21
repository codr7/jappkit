package codr7.jappkit.db;

import java.nio.channels.SeekableByteChannel;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Record extends ConstRecord {
    public Record init(Relation it) {
        it.init(this);
        return this;
    }

    public void setObject(Column<?> column, Object value) { fields.put(column, value); }

    public <ValueT> Record set(Column<ValueT> column, ValueT value) {
        setObject(column, column.set(value));
        return this;
    }
}
