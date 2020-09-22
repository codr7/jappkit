package codr7.jappkit.db;

public class Rec extends ConstRec {
    public Rec init(Relation it) {
        it.init(this);
        return this;
    }

    public void setObject(Col<?> col, Object value) { fields.put(col, value); }

    public <ValueT> Rec set(Col<ValueT> col, ValueT value) {
        setObject(col, col.set(value));
        return this;
    }
}
