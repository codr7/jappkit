package codr7.jappkit.db.columns;

import codr7.jappkit.db.*;

import java.nio.channels.SeekableByteChannel;

public class StringColumn extends Column<String> {
    public static final ColumnType<String> type = new ColumnType<String>() {
        @Override
        public Cmp cmp(String x, String y) {
            return Cmp.valueOf(x.compareTo(y));
        }

        @Override
        public Object load(SeekableByteChannel in) { return Encoding.readString(in); }

        @Override
        public void store(Object it, SeekableByteChannel out) { Encoding.writeString((String)it, out); }
    };

    public StringColumn(Table table, String name) {
        super(table, type, name);
    }

}
