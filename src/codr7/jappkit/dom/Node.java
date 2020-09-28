package codr7.jappkit.dom;

import java.io.OutputStream;
import java.io.PrintStream;

public interface Node {

    void write(PrintStream out, boolean pretty, int depth);
    default void write(OutputStream out, boolean pretty) { write(new PrintStream(out), pretty, 0); }
    default void write(OutputStream out) { write(out, false); }
}
