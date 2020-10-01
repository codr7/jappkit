package codr7.jappkit.db;

import codr7.jappkit.E;
import jdk.jfr.RecordingState;

import java.nio.channels.SeekableByteChannel;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Stream;

public class ConstRec {
    public static final ConstRec DELETED = new ConstRec();

    public Rec clone() {
        Rec it = new Rec();

        for (Map.Entry<Col<?>, Object> i: fields.entrySet()) {
            Col c = i.getKey();
            it.setObject(c, c.clone(i.getValue()));
        }

        return it;
    }

    public boolean contains(Col<?> it) { return fields.containsKey(it); }

    public Object getObject(Col<?> it) { return fields.get(it); }

    public <ValueT> ValueT get(Col<ValueT> it) { return it.get((ValueT) getObject(it)); }

    public Stream<Map.Entry<Col<?>, Object>> fields() { return fields.entrySet().stream(); }

    public void write(SeekableByteChannel out) {
        int len = fields.size();
        Encoding.writeLong(len, out);

        for (Map.Entry<Col<?>, Object> f: fields.entrySet()) {
            Col<?> c = f.getKey();
            Encoding.writeString(c.name, out);
            c.store(f.getValue(), out);
        }
    }

    protected final Map<Col<?>, Object> fields = new TreeMap<>(Comparator.comparing(Object::toString));
}
