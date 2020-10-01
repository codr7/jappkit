package codr7.jappkit;

import java.time.Instant;

public class Time {
    public static final long M = 60;
    public static final long H = M * 60;
    public static final long DAY = H * 24;

    public static Instant today() { return Instant.ofEpochSecond(Instant.now().getEpochSecond() / DAY); }
    private Time() {}
}
