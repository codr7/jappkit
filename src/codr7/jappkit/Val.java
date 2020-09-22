package codr7.jappkit;

public class Val {
    public Type<?> type;
    public Object data;

    public static <ValT> Val make(Type<ValT> type, ValT data) {
        return new Val(type, data);
    }

    public Val cp() { return new Val(type, data); }

    public Val clone() { return new Val(type, type.cloneObject(data)); }

    public <ValT> ValT as(Type<ValT> type) { return this.type.isa(type) ? type.get((ValT)data) : null; }

    public void swap(Val it) {
        Type<?> tt = it.type;
        Object td = it.data;

        it.type = type;
        it.data = data;

        type = tt;
        data = td;
    }

    private Val(Type<?> type, Object data) {
        this.type = type;
        this.data = data;
    }
}