package codr7.jappkit.types;

import codr7.jappkit.Cmp;
import codr7.jappkit.E;
import codr7.jappkit.Type;
import codr7.jappkit.db.Encoding;

import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SeekableByteChannel;
import java.util.Iterator;
import java.util.Set;

public final class SetType<ItemT> extends Type<Set<ItemT>> {
    public final Class setClass;
    public final Type<ItemT> itemType;

    public SetType(Class setClass, Type<ItemT> itemType) {
        super(String.format("Set<%s>", itemType.name));
        this.setClass = setClass;
        this.itemType = itemType;
    }

    @Override
    public Set<ItemT> init() {
        try { return (Set<ItemT>)setClass.getConstructor().newInstance(); }
        catch (IllegalAccessException |
                InstantiationException |
                InvocationTargetException |
                NoSuchMethodException e) {  throw new E(e); }
    }

    @Override
    public Set<ItemT> clone(Set<ItemT> it) {
        Set<ItemT> out = init();
        for (ItemT i: it) { out.add(itemType.clone(i)); }
        return out;
    }

    @Override
    public Object load(SeekableByteChannel in) {
        Set<ItemT> out = init();
        int len = (int) Encoding.readLong(in);
        for (int i = 0; i < len; i++) { out.add((ItemT)itemType.load(in)); }
        return out;
    }

    @Override
    public void store(Object it, SeekableByteChannel out) {
        Set<ItemT> is = (Set<ItemT>)it;
        Encoding.writeLong(is.size(), out);
        for (ItemT i: is) { itemType.store(i, out); }
    }

    @Override
    public Cmp cmp(Set<ItemT> x, Set<ItemT> y) {
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
