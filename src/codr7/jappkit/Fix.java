package codr7.jappkit;

public class Fix {
    public static final long SCALE = 1000;

    public static long make(long in) { return in * SCALE; }

    private Fix() {}
}
