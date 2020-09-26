package codr7.jappkit.error;

import codr7.jappkit.E;

import java.io.IOException;

public class EOF extends E {
    public EOF(IOException cause) { super(cause); }
    public EOF() { super("End of file"); }
}