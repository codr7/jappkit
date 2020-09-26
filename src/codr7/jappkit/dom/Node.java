package codr7.jappkit.dom;

import java.io.OutputStream;
import java.io.PrintStream;
import java.util.Map;
import java.util.stream.Stream;

public interface Node {
    String tag();
    Stream<Map.Entry<String, Object>> attrs();
    Stream<Node> childNodes();

    default void write(PrintStream out) {
        String t = tag();
        out.printf("<%s", t);
        Stream<Map.Entry<String, Object>> as = attrs();
        if (as != null) { as.forEach((a) -> out.printf(" %s=\"%s\"", a.getKey(), a.getValue())); }
        Stream<Node> cns = childNodes();

        if (cns == null) { out.print("/>\n"); } else {
            out.print(">\n");
            cns.forEach((cn) -> { cn.write(out); });
            out.printf("</%s>\n", t);
        }
    }
}
