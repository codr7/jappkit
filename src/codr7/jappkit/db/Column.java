package codr7.jappkit.db;

import java.nio.channels.SeekableByteChannel;

public abstract class Column<ValueT> {
    public final Table table;
    public final String name;

    public abstract Cmp cmp(ValueT x, ValueT y);
    public abstract Object load(SeekableByteChannel in);
    public abstract void store(Object it, SeekableByteChannel out);

    protected Column(Table table, String name) {
        this.table = table;
        this.name = name;
        table.addColumn(this);
    }
}