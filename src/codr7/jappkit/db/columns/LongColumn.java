package codr7.jappkit.db.columns;

import codr7.jappkit.db.*;

import java.nio.channels.SeekableByteChannel;

public class LongColumn extends Column<Long> {
    public static final ColumnType<Long> type = new ColumnType<>() {
        @Override
        public Long init() { return 0L; }

        @Override
        public Object load(SeekableByteChannel in) { return Encoding.readLong(in); }

        @Override
        public void store(Object it, SeekableByteChannel out) { Encoding.writeLong(((Long)it).longValue(), out); }

        @Override
        public Cmp cmp(Long x, Long y) { return Cmp.valueOf(x.compareTo(y)); }
    };

    public LongColumn(Table table, String name) {
        super(table, type, name);
    }
}
