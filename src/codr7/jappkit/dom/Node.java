package codr7.jappkit.dom;

import java.io.OutputStream;
import java.io.PrintStream;

public interface Node {
    void write(PrintStream out);
    default void write(OutputStream out) { write(new PrintStream(out)); }
}
