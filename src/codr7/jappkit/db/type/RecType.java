package codr7.jappkit.db.type;

import codr7.jappkit.Cmp;
import codr7.jappkit.Type;
import codr7.jappkit.db.*;

import java.nio.channels.SeekableByteChannel;

public final class RecType extends Type<Prec> {
    public final Table refTable;

    public RecType(Table refTable) {
        super(String.format("Rec<%s>", refTable.name));
        this.refTable = refTable;
    }

    @Override
    public Prec init() { return new Prec(refTable, -1); }

    @Override
    public Prec clone(Prec it) { return new Prec(it.refTable, it.id); }

    @Override
    public Object load(SeekableByteChannel in) {
        long id = Encoding.readLong(in);
        return new Prec(refTable, id);
    }

    @Override
    public void store(Object it, SeekableByteChannel out) {
        long id = Rec.class.isAssignableFrom(it.getClass()) ? ((ConstRec)it).get(refTable.id) : ((Prec)it).id;
        Encoding.writeLong(id, out);
    }

    @Override
    public Cmp cmp(Prec x, Prec y) { return Cmp.valueOf(Long.compare(x.id, y.id)); }
}
