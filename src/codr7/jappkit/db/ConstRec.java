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

    public boolean isDirty(Tx tx, Col<?>...cols) {
        var cs = (cols.length == 0) ? fields().map((e) -> e.getKey()) : Arrays.stream(cols);
        var prs = new TreeMap<Table, ConstRec>();

        return cs.filter((Col<?> c) -> {
            var pr = prs.get(c.table);

            if (pr == null) {
                pr = c.table.load(get(c.table.id), tx);
                if (pr == null) { prs.put(c.table, Rec.DELETED); }
                prs.put(c.table, pr);
            } else if (pr == Rec.DELETED) { return false; }

            return !getObject(c).equals(pr.getObject(c));
        }).iterator().hasNext();
    }

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
