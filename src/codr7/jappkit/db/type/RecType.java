package codr7.jappkit.db.type;

import codr7.jappkit.Cmp;
import codr7.jappkit.Type;
import codr7.jappkit.db.*;

import java.nio.channels.SeekableByteChannel;

public final class RecType extends Type<RecProxy> {
    public final Table refTable;

    public RecType(Table refTable) {
        super(String.format("Rec<%s>", refTable.name));
        this.refTable = refTable;
    }

    @Override
    public RecProxy init() { return new RecProxy(refTable, -1); }

    @Override
    public RecProxy clone(RecProxy it) { return new RecProxy(it.refTable, it.id); }

    @Override
    public Object load(SeekableByteChannel in) {
        long id = Encoding.readLong(in);
        return new RecProxy(refTable, id);
    }

    @Override
    public void store(Object it, SeekableByteChannel out) {
        long id = ConstRec.class.isAssignableFrom(it.getClass()) ? ((ConstRec)it).get(refTable.id) : ((RecProxy)it).id;
        Encoding.writeLong(id, out);
    }

    @Override
    public Cmp cmp(RecProxy x, RecProxy y) { return Cmp.valueOf(Long.compare(x.id, y.id)); }
}
