package codr7.jappkit;

import java.io.BufferedReader;
import java.io.EOFException;
import java.io.IOException;
import java.io.PrintStream;

public abstract class Repl {
    public void run(BufferedReader in, PrintStream out) {
        for (;;) {
            try {
                var line = readLine(in, out);
                processLine(line, out);
            }
            catch (EOFException e) { break; }
            catch (IOException e) { throw new E(e); }
        }
    }

    protected String readLine(BufferedReader in, PrintStream out) throws IOException { return in.readLine(); }

    protected abstract void processLine(String in, PrintStream out);
}
