package codr7.jappkit.dom;

import java.io.OutputStream;
import java.io.PrintStream;

public interface Node {
    enum WriteOpt {
        Pretty(1);
        final int as_int;
        WriteOpt(int as_int) { this.as_int = as_int; }
    }

    void write(PrintStream out, int opts, int depth);
    default void write(OutputStream out, int opts) { write(new PrintStream(out), opts, 0); }
    default void write(OutputStream out) { write(out, 0); }
}
