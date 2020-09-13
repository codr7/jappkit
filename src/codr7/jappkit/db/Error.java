package codr7.jappkit.db;

public class Error extends RuntimeException {
    public Error(String spec, Object...args) {
        super(String.format(spec, args));
    }
}
