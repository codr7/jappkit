package codr7.jappkit.dom;

import codr7.jappkit.dom.Node;

import java.io.PrintStream;
import java.util.Map;
import java.util.stream.Stream;

public interface Tag extends Node {
    String tag();
    Stream<Map.Entry<String, Object>> attrs();
    Stream<Node> childNodes();

    @Override
    default void write(PrintStream out) {
        String t = tag();
        out.printf("<%s", t);
        Stream<Map.Entry<String, Object>> as = attrs();

        if (as != null) {
            as.forEach((a) -> {
                out.printf(" %s", a.getKey());
                Object v = a.getValue();
                if (v != null) { out.printf("=\"%s\"", a.getValue()); }
            });
        }

        Stream<Node> cns = childNodes();

        if (cns == null) { out.print("/>\n"); } else {
            out.print(">\n");
            cns.forEach((cn) -> { cn.write(out); });
            out.printf("</%s>\n", t);
        }
    }
}
