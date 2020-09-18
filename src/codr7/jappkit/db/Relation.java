package codr7.jappkit.db;

import java.time.Instant;

public abstract class Relation implements Comparable<Relation> {
    public final Schema schema;
    public final String name;

    public abstract Relation addColumn(Column<?> it);
    public abstract void open(Instant maxTime);
    public abstract void close();

    protected Relation(Schema schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    @Override
    public int compareTo(Relation other) { return name.compareTo(other.name); }
}