package codr7.jappkit.db;

import codr7.jappkit.E;

import java.nio.channels.SeekableByteChannel;

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
}
