package codr7.jappkit;

import java.time.Instant;

public abstract class Definition {
    public final Schema schema;
    public final String name;

    public abstract void open(Instant maxTime);
    public abstract void close();

    protected Definition(Schema schema, String name) {
        this.schema = schema;
        this.name = name;
    }
}