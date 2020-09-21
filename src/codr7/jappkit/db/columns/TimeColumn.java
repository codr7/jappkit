package codr7.jappkit.db.columns;

import codr7.jappkit.db.*;

import java.nio.channels.SeekableByteChannel;
import java.time.Instant;

public class TimeColumn extends Column<Instant> {
    public static final ColumnType<Instant> type = new ColumnType<>() {
        @Override
        public Instant init() { return Instant.ofEpochMilli(0L); }

        @Override
        public Instant set(Instant it) { return Instant.ofEpochMilli(it.toEpochMilli()); }

        @Override
        public Object load(SeekableByteChannel in) { return Encoding.readTime(in); }

        @Override
        public void store(Object it, SeekableByteChannel out) { Encoding.writeTime((Instant)it, out); }

        @Override
        public Cmp cmp(Instant x, Instant y) { return Cmp.valueOf(x.compareTo(y)); }
    };

    public TimeColumn(Table table, String name) {
        super(table, type, name);
    }
}