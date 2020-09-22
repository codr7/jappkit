package codr7.jappkit.errors;

import codr7.jappkit.E;

import java.io.IOException;

public class EOF extends E {
    public EOF(IOException cause) { super(cause); }
    public EOF() { super("End of file"); }
}