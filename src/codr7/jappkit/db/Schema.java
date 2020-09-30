package codr7.jappkit.db;

import java.nio.file.Path;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static codr7.jappkit.Utils.*;

public class Schema {
    public final Path root;

    public Schema(Path root) {
        this.root = root;
    }

    public void addIndex(Index it) {
        indexes.add(it);
    }

    public void addTable(Table it) {
        tables.add(it);
        tableLookup.put(it.name, it);
    }

    public Table findTable(String name) { return tableLookup.get(name); }

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
    private final Map<String, Table> tableLookup = new HashMap<>();
    private final List<Index> indexes = new ArrayList<>();
}
