package codr7.jappkit.db;

public enum Cmp {LT(-1), EQ(0), GT(1);
    public static Cmp valueOf(int it) {
        switch (it) {
            case -1:
                return LT;
            case 1:
                return GT;
            default:
                break;
        }

        return EQ;
    }

    public final int asInt;

    Cmp(int intResult) {
        this.asInt = intResult;
    }
};

