package codr7.jappkit.db;

import java.nio.channels.SeekableByteChannel;

public class Column<ValueT> {
    public final Table table;
    public final ColumnType<ValueT> type;
    public final String name;

    public Column(Table table, ColumnType<ValueT> type, String name) {
        this.table = table;
        this.type = type;
        this.name = name;

        if (!name.equals("id")) { table.addColumn(this); }
    }

    public Cmp cmp(ValueT x, ValueT y) { return type.cmp(x, y); }

    public ValueT init() { return type.init(); }
    public Object initObject() { return init(); }

    public ValueT get(ValueT it) { return type.get(it); }
    public ValueT set(ValueT it) { return type.set(it); }

    public Object load(SeekableByteChannel in) { return type.load(in); }
    public void store(Object it, SeekableByteChannel out) { type.store(it, out); }
}