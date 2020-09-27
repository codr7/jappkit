package codr7.jappkit.db;

import codr7.jappkit.E;

import java.lang.reflect.Field;

public abstract class Mod {
    public final long id;

    public Mod(long id) { this.id = id; }

    public Mod(Rec in) {
        Table t = table();
        this.id = in.get(t.id);

        t.cols().forEach((c) -> {
            Field f = null;
            try { f = getClass().getDeclaredField(c.name); } catch (NoSuchFieldException e) { throw new E(e); }
            f.setAccessible(true);
            try { f.set(this, in.get(c)); } catch (IllegalAccessException e) { throw new E(e); }
        });
    }

    public abstract Table table();

    public void store(Tx tx) { table().store(toRec(), tx); }

    public Rec toRec() {
        Table t = table();
        Rec out = new Rec();
        out.set(t.id, id);

        t.cols().forEach((c) -> {
            Field f = null;
            try { f = getClass().getDeclaredField(c.name); } catch (NoSuchFieldException e) { throw new E(e); }
            f.setAccessible(true);
            try { out.setObject(c, f.get(this)); } catch (IllegalAccessException e) { throw new E(e); }
        });

        return out;
    }
}
