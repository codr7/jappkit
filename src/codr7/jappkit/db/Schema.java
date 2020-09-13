package codr7.jappkit.db;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import static codr7.jappkit.db.Utils.*;

public class Schema {
    public final String root;

    public Schema(String root) {
        this.root = root;
    }

    public void addIndex(Index it) {
        indexes.add(it);
    }

    public void addTable(Table it) {
        tables.add(it);
    }

    public void drop() {
        rmdir(root);
    }

    public void open(Instant maxTime) {
        mkdir(root);
        for (Index it: indexes) { it.open(maxTime); }
        for (Table it: tables) { it.open(maxTime); }
    }

    public void close() {
        for (Index it: indexes) { it.close(); }
        for (Table it: tables) { it.close(); }
    }

    private final List<Table> tables = new ArrayList<>();
    private final List<Index> indexes = new ArrayList<>();
}
