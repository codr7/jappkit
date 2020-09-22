package codr7.jappkit;

public class E extends RuntimeException {
    public E(String msg, Object...args) { super(String.format(msg, args)); }
    public E(Throwable cause) { super(cause); }
}
