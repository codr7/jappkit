package codr7.jappkit.db;

import java.time.Instant;

public abstract class Relation {
    public final Schema schema;
    public final String name;

    public abstract void addColumn(Column<?> it);
    public abstract void open(Instant maxTime);
    public abstract void close();

    protected Relation(Schema schema, String name) {
        this.schema = schema;
        this.name = name;
    }
}