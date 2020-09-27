package codr7.jappkit.db;

import java.nio.file.StandardOpenOption;
import java.time.Instant;
import java.util.stream.Stream;

public abstract class Relation implements Comparable<Relation> {
    public static final StandardOpenOption[] fileOptions = {
            StandardOpenOption.CREATE,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE,
            StandardOpenOption.SYNC
    };

    public final Schema schema;
    public final String name;

    public abstract Relation addCol(Col<?> it);
    public abstract Stream<Col<?>> cols();
    public abstract void open(Instant maxTime);
    public abstract void close();
    public abstract void init(Rec it, Col<?>...cols);

    protected Relation(Schema schema, String name) {
        this.schema = schema;
        this.name = name;
    }

    @Override
    public int compareTo(Relation other) { return name.compareTo(other.name); }
}