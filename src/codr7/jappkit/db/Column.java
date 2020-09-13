package codr7.jappkit.db;

public abstract class Column<ValueT> {
    public final String name;

    public abstract Cmp cmp(ValueT x, ValueT y);

    protected Column(String name) {
        this.name = name;
    }
}