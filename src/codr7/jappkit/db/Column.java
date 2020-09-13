package codr7.jappkit.db;

public abstract class Column<ValueT> {
    public final Table table;
    public final String name;

    public abstract Cmp cmp(ValueT x, ValueT y);

    protected Column(Table table, String name) {
        this.table = table;
        this.name = name;
        table.addColumn(this);
    }
}