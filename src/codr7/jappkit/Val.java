package codr7.jappkit;

public class Val {
    public final Type<?> type;
    public final Object data;

    public static <ValT> Val make(Type<ValT> type, ValT data) {
        return new Val(type, data);
    }

    public Val clone() { return new Val(type, type.cloneObject(data)); }

    private Val(Type<?> type, Object data) {
        this.type = type;
        this.data = data;
    }
}
