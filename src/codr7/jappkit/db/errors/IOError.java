package codr7.jappkit.db.errors;

import codr7.jappkit.db.E;

import java.io.IOException;

public class IOError extends E {
    public IOError(IOException cause) {
        super(cause.getMessage());
        this.cause = cause;
    }

    private final IOException cause;
}
