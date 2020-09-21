package codr7.jappkit.db;

import java.nio.channels.SeekableByteChannel;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class Record {
    public static final Record DELETED = new Record();

    public Record init(Relation it) {
        it.init(this);
        return this;
    }

    public Record clone() {
        Record it = new Record();

        for (Map.Entry<Column<?>, Object> i: fields.entrySet()) {
            Column c = i.getKey();
            it.setObject(c, c.clone(i.getValue()));
        }

        return it;
    }

    public boolean contains(Column<?> it) { return fields.containsKey(it); }

    public Object getObject(Column<?> it) { return fields.get(it); }
    public <ValueT> ValueT get(Column<ValueT> it) { return it.get((ValueT) getObject(it)); }

    public void setObject(Column<?> column, Object value) { fields.put(column, value); }

    public <ValueT> Record set(Column<ValueT> column, ValueT value) {
        setObject(column, column.set(value));
        return this;
    }

    public Stream<Map.Entry<Column<?>, Object>> fields() { return fields.entrySet().stream(); }

    public void write(SeekableByteChannel out) {
        int len = fields.size();

        for (Map.Entry<Column<?>, Object> f: fields.entrySet()) {
            if (f.getKey().isVirtual) { len--; }
        }

        Encoding.writeLong(len, out);

        for (Map.Entry<Column<?>, Object> f: fields.entrySet()) {
            Column<?> c = f.getKey();
            Encoding.writeString(c.name, out);
            c.store(f.getValue(), out);
        }
    }

    private final Map<Column<?>, Object> fields = new TreeMap<>(Comparator.comparing(Object::toString));
}
