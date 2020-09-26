package codr7.jappkit.dom.html;

import codr7.jappkit.dom.CustomTag;

public class Table extends HTMLTag {
    public static class Row extends HTMLTag {
        public Row() { super("tr"); }

        public HTMLTag td() { return appendTag("td"); }
    }

    public Table() { super("table"); }

    public Row tr() {
        Row it = new Row();
        append(it);
        return it;
    }
}
