package codr7.jappkit.types;

import codr7.jappkit.Cmp;
import codr7.jappkit.Type;
import codr7.jappkit.E;
import codr7.jappkit.db.Encoding;

import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SeekableByteChannel;
import java.util.Map;

public final class MapType<KeyT, ValueT> extends Type<Map<KeyT, ValueT>> {
    public final Class mapClass;
    public final Type<KeyT> keyType;
    public final Type<ValueT> valueType;

    public MapType(Class mapClass, Type<KeyT> keyType, Type<ValueT> valueType) {
        super("Map");
        this.mapClass = mapClass;
        this.keyType = keyType;
        this.valueType = valueType;
    }

    @Override
    public Map<KeyT, ValueT> init() {
        try { return (Map<KeyT, ValueT>)mapClass.getConstructor().newInstance(); }
        catch (IllegalAccessException |
                InstantiationException |
                InvocationTargetException |
                NoSuchMethodException e) {  throw new E(e); }
    }

    @Override
    public Map<KeyT, ValueT> clone(Map<KeyT, ValueT> it) {
        Map<KeyT, ValueT> out = init();

        for (Map.Entry<KeyT, ValueT> i: it.entrySet()) {
            out.put(keyType.clone(i.getKey()), valueType.clone(i.getValue()));
        }

        return out;
    }

    @Override
    public Object load(SeekableByteChannel in) {
        Map<KeyT, ValueT> out = init();
        int len = (int) Encoding.readLong(in);
        for (int i = 0; i < len; i++) { out.put((KeyT)keyType.load(in), (ValueT)valueType.load(in)); }
        return out;
    }

    @Override
    public void store(Object it, SeekableByteChannel out) {
        Map<KeyT, ValueT> m = (Map<KeyT, ValueT>)it;
        Encoding.writeLong(m.size(), out);

        for (Map.Entry<KeyT, ValueT> i: m.entrySet()) {
            keyType.store(i.getKey(), out);
            valueType.store(i.getValue(), out);
        }
    }

    @Override
    public Cmp cmp(Map<KeyT, ValueT> x, Map<KeyT, ValueT> y) {
        for (Map.Entry<KeyT, ValueT> i: x.entrySet()) {
            Object j = y.get(i.getKey());
            if (j == null) { return Cmp.GT; }
            Cmp result = valueType.cmp(i.getValue(), (ValueT)j);
            if (result != Cmp.EQ) { return result; }
        }

        return Cmp.valueOf(Integer.valueOf(x.size()).compareTo(Integer.valueOf(y.size())));
    }
}
