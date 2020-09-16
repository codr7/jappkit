package codr7.jappkit.db.columns;

import codr7.jappkit.db.*;

import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SeekableByteChannel;
import java.util.Iterator;
import java.util.Map;

public class MapColumn <KeyT, ValueT> extends Column<Map<KeyT, ValueT>> {
    public static final class Type <KeyT, ValueT> implements ColumnType<Map<KeyT, ValueT>> {
        public final Class mapClass;
        public final ColumnType<KeyT> keyType;
        public final ColumnType<ValueT> valueType;

        public Type(Class mapClass, ColumnType<KeyT> keyType, ColumnType<ValueT> valueType) {
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
        public Object load(SeekableByteChannel in) {
            Map<KeyT, ValueT> out = init();
            int len = (int)Encoding.readLong(in);
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
    };

    public MapColumn(Table table, String name, Class MapClass, ColumnType<KeyT> keyType, ColumnType<ValueT> valueType) {
        super(table, new Type<>(MapClass, keyType, valueType), name);
    }
}