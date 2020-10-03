package codr7.jappkit.demo.bookr;

public class Amount {
    public static final long multiplier = 1000;

    public static long make(long in) { return in * multiplier; }

    private Amount() {}
}
