package codr7.jappkit.dom;

import java.io.PrintStream;

public class TextNode implements Node {
    public final String text;

    public TextNode(String text) { this.text = text; }

    @Override
    public void write(PrintStream out, boolean pretty, int depth) { out.print(text); }
}