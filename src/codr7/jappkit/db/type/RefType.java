package codr7.jappkit.db.type;

import codr7.jappkit.Cmp;
import codr7.jappkit.Type;
import codr7.jappkit.db.*;

import java.nio.channels.SeekableByteChannel;

public final class RefType <T> extends Type<Ref<T>> {
    public final Table refTable;
    public final Mod.Make<T> make;

    public RefType(Table refTable, Mod.Make<T> make) {
        super(String.format("Ref<%s>", refTable.name));
        this.refTable = refTable;
        this.make = make;
    }

    @Override
    public Ref<T> init() { return new Ref<T>(refTable, -1, make); }

    @Override
    public Ref<T> clone(Ref<T> it) { return new Ref<T>(it.refTable, it.id, make); }

    @Override
    public Object load(SeekableByteChannel in) {
        long id = Encoding.readLong(in);
        return new Ref<T>(refTable, id, make);
    }

    @Override
    public void store(Object it, SeekableByteChannel out) {
        long id = Mod.class.isAssignableFrom(it.getClass()) ? ((Mod)it).id : ((Ref<?>)it).id;
        Encoding.writeLong(id, out);
    }

    @Override
    public Cmp cmp(Ref<T> x, Ref<T> y) { return Cmp.valueOf(Long.compare(x.id, y.id)); }
}
