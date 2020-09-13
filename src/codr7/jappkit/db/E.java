package codr7.jappkit.db;

public class E extends RuntimeException {
    public E(String spec, Object...args) {
        super(String.format(spec, args));
    }
}
