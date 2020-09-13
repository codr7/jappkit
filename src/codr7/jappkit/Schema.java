package codr7.jappkit;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static codr7.jappkit.Utils.*;

public class Schema {
    public final String root;

    public Schema(String root) {
        this.root = root;
    }

    public void addIndex(Index _) {
        indexes.add(_);
    }

    public void addTable(Table _) {
        tables.add(_);
    }

    public void drop() {
        rmdir(root);
    }

    public void open(Instant maxTime) {
        mkdir(root);
        for (Index _: indexes) { _.open(maxTime); }
        for (Table _: tables) { _.open(maxTime); }
    }

    public void close() {
        for (Index _: indexes) { _.close(); }
        for (Table _: tables) { _.close(); }
    }

    private final List<Table> tables = new ArrayList<>();
    private final List<Index> indexes = new ArrayList<>();
}
