package codr7.jappkit.db;

import codr7.jappkit.E;

import java.nio.channels.SeekableByteChannel;
import java.util.Map;

public class Rec extends ConstRec {
    public Rec init(Relation it) {
        it.init(this);
        return this;
    }

    public void setObject(Col<?> col, Object value) { fields.put(col, value); }

    public <ValueT> Rec set(Col<ValueT> col, ValueT value) {
        setObject(col, col.set(value));
        return this;
    }

    public void load(SeekableByteChannel in, Table table) {
        long len = Encoding.readLong(in);

        for (long i = 0; i < len; i++) {
            String cn = Encoding.readString(in);
            Col<?> c = table.findCol(cn);
            if (c == null) { throw new E("Unknown column: %s", cn); }
            setObject(c, c.load(in));
        }
    }

    @Override
    public String toString() {
        StringBuilder buf = new StringBuilder("Rec(");

        for (Map.Entry<Col<?>, Object> f: fields.entrySet()) {
            if (buf.length() > 4) { buf.append(", "); }
            Col<?> c = f.getKey();
            buf.append(c.table.name);
            buf.append('.');
            buf.append(c.name);
            buf.append(": ");
            buf.append(c.toString(f.getValue()));
        }

        buf.append(')');
        return buf.toString();
    }
}
