package codr7.jappkit.db;

import codr7.jappkit.E;
import codr7.jappkit.demo.bookr.DB;

import java.lang.reflect.Field;

public abstract class Mod {
    public interface Make<T> {
        T call(Rec rec);
    }

    public final Table table;
    public final long id;

    public static Field getField(Class<?> owner, String name) {
        while (true) {
            try {
                Field f = owner.getDeclaredField(name);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException e) {
                if (owner == Object.class) { throw new E(e); }
                owner = owner.getSuperclass();
            }
        }
    }

    public Mod(Table table) {
        this.table = table;
        this.id = table.getNextRecId();

        table.cols().forEach((c) -> {
            Field f = getField(getClass(), c.name);
            try { f.set(this, c.init()); } catch (IllegalAccessException e) { throw new E(e); }
        });
    }

    public Mod(Table table, Rec in) {
        this.table = table;
        this.id = in.get(table.id);
        load(in);
    }

    public void load(Rec in) {
        table.cols().forEach((c) -> {
            Field f = getField(getClass(), c.name);
            Object v = in.get(c);
            try { f.set(this, (v == null) ? c.init(): v); } catch (IllegalAccessException e) { throw new E(e); }
        });
    }

    public void reload(Tx tx) { load(table.load(id, tx)); }

    public void store(Tx tx) { table.store(toRec(), tx); }

    public Rec toRec() {
        Rec out = new Rec();
        out.set(table.id, id);

        table.cols().forEach((c) -> {
            Field f = getField(getClass(), c.name);
            try { out.setObject(c, f.get(this)); } catch (IllegalAccessException e) { throw new E(e); }
        });

        return out;
    }

    @Override
    public String toString() { return toRec().toString(); }
}
