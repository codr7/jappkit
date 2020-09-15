package codr7.jappkit.db.columns;

import codr7.jappkit.db.Cmp;
import codr7.jappkit.db.Column;
import codr7.jappkit.db.Encoding;
import codr7.jappkit.db.Table;

import java.nio.channels.SeekableByteChannel;

public class StringColumn extends Column<String> {
    public StringColumn(Table table, String name) {
        super(table, name);
    }

    @Override
    public Cmp cmp(String x, String y) {
        return Cmp.valueOf(x.compareTo(y));
    }

    @Override
    public Object load(SeekableByteChannel in) { return Encoding.readString(in); }

    @Override
    public void store(Object it, SeekableByteChannel out) { Encoding.writeString((String)it, out); }
}
