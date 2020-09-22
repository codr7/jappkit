package codr7.jappkit.db;

import codr7.jappkit.Cmp;
import codr7.jappkit.Type;

import java.nio.channels.SeekableByteChannel;

public class Column<ValueT> {
    public final Table table;
    public final Type<ValueT> type;
    public final String name;
    public boolean isVirtual = false;

    public Column(Table table, Type<ValueT> type, String name) {
        this.table = table;
        this.type = type;
        this.name = name;

        table.addColumn(this);
    }

    public Cmp cmp(ValueT x, ValueT y) { return type.cmp(x, y); }
    public Cmp cmpObject(Object x, Object y) { return cmp((ValueT)x, (ValueT)y); }

    public ValueT init() { return type.init(); }
    public Object initObject() { return init(); }

    public ValueT clone(ValueT it) { return type.clone(it); }
    public Object cloneObject(Object it) { return clone((ValueT)it); }

    public ValueT get(ValueT it) { return type.get(it); }
    public ValueT set(ValueT it) { return type.set(it); }

    public Object load(SeekableByteChannel in) { return type.load(in); }
    public void store(Object it, SeekableByteChannel out) { type.store(it, out); }
}