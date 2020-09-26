package codr7.jappkit.type;

import codr7.jappkit.Cmp;
import codr7.jappkit.Type;
import codr7.jappkit.E;
import codr7.jappkit.db.Encoding;

import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SeekableByteChannel;
import java.util.Iterator;
import java.util.List;

public final class ListType<ItemT> extends Type<List<ItemT>> {
    public final Class listClass;
    public final Type<ItemT> itemType;

    public ListType(Class listClass, Type<ItemT> itemType) {
        super(String.format("List<%s>", itemType.name));
        this.listClass = listClass;
        this.itemType = itemType;
    }

    @Override
    public List<ItemT> init() {
        try { return (List<ItemT>)listClass.getConstructor().newInstance(); }
        catch (IllegalAccessException |
                InstantiationException |
                InvocationTargetException |
                NoSuchMethodException e) {  throw new E(e); }
    }

    @Override
    public List<ItemT> clone(List<ItemT> it) {
        List<ItemT> out = init();
        for (ItemT i: it) { out.add(itemType.clone(i)); }
        return out;
    }

    @Override
    public Object load(SeekableByteChannel in) {
        List<ItemT> out = init();
        int len = (int) Encoding.readLong(in);
        for (int i = 0; i < len; i++) { out.add((ItemT)itemType.load(in)); }
        return out;
    }

    @Override
    public void store(Object it, SeekableByteChannel out) {
        List<ItemT> is = (List<ItemT>)it;
        Encoding.writeLong(is.size(), out);
        for (ItemT i: is) { itemType.store(i, out); }
    }

    @Override
    public Cmp cmp(List<ItemT> x, List<ItemT> y) {
        int max = Math.min(x.size(), y.size()), i = 0;
        Iterator<ItemT> xi = null, yi = null;

        for (xi = x.iterator(), yi = y.iterator();
             xi.hasNext() && yi.hasNext();) {
            Cmp result = itemType.cmp(xi.next(), yi.next());
            if (result != Cmp.EQ) { return result; }
        }

        if (yi.hasNext()) { return Cmp.LT; }
        if (xi.hasNext()) { return Cmp.GT; };
        return Cmp.EQ;
    }
}
