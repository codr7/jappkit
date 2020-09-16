package codr7.jappkit.db.columns;

import codr7.jappkit.db.*;

import java.lang.reflect.InvocationTargetException;
import java.nio.channels.SeekableByteChannel;
import java.security.DrbgParameters;
import java.util.Iterator;
import java.util.List;

public class ListColumn <ItemT> extends Column<List<ItemT>> {
    public static final class Type <ItemT> implements ColumnType<List<ItemT>> {
        public final Class listClass;
        public final ColumnType<ItemT> itemType;

        public Type(Class listClass, ColumnType<ItemT> itemType) {
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
        public Object load(SeekableByteChannel in) {
            List<ItemT> out = init();
            int len = (int)Encoding.readLong(in);
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
    };

    public ListColumn(Table table, String name, Class listClass, ColumnType<ItemT> itemType) {
        super(table, new Type<>(listClass, itemType), name);
    }
}
