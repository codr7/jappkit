package codr7.jappkit.dom;

import java.io.PrintStream;
import java.util.EnumSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

public interface Tag extends Node {
    String tag();
    Stream<Map.Entry<String, Object>> attrs();
    Stream<Node> childNodes();

    @Override
    default void write(PrintStream out, int opts, int depth) {
        String t = tag();
        boolean pretty = (opts & WriteOpt.Pretty.as_int) != 0;

        System.out.println("pretty: " + pretty + " depth: " + depth);

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

        if (cns == null) {
            out.print("/>");
            if (pretty) { out.print('\n'); }
        } else {
            out.print(">");
            if (pretty) { out.print('\n'); }

            cns.forEach((cn) -> {
                int cd = depth+1;

                if (pretty) {
                    for (int i = 0; i < cd; i++) { out.print("  "); }
                }

                cn.write(out, opts, cd);
            });

            out.printf("</%s>", t);
            if (pretty) { out.print('\n'); }
        }
    }
}
