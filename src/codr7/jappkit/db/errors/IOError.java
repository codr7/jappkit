package codr7.jappkit.db.errors;

import codr7.jappkit.db.Error;

import java.io.IOException;

public class IOError extends Error {
    public IOError(IOException cause) {
        super(cause.getMessage());
        this.cause = cause;
    }

    private final IOException cause;
}
