package codr7.jappkit.db.errors;

import codr7.jappkit.db.E;

import java.io.IOException;

public class EIO extends E {
    public EIO(IOException cause) { super(cause); }
}
