package codr7.jappkit.lang;

import codr7.jappkit.Type;
import codr7.jappkit.Val;

import java.util.Map;
import java.util.TreeMap;

public class Env {
    public void set(String key, Val val) { items.put(key, val); }

    public <ValT> void set(String key, Type<ValT> type, ValT data) { items.put(key, Val.make(type, data)); }

    public Val get(String key) { return items.get(key); }

    private Map<String, Val> items = new TreeMap<>();
}
