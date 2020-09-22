package codr7.jappkit;

import codr7.jappkit.Type;
import codr7.jappkit.Val;

import java.util.ArrayList;

public class Stack {
    public void drop(int i, int n) {
        i = items.size() - i - 1;
        while (n-- > 0) { items.remove(i); }
    }

    public Val push(Val it) {
        items.add(it);
        return it;
    }

    public <ValT> Val push(Type<ValT> type, ValT...datas) {
        Val v = null;
        for (ValT d: datas) { push((v = Val.make(type, d))); }
        return v;
    }

    public Val peek(int offs) { return items.get(items.size() - offs - 1); }

    public Val peek() { return peek(0); }

    public <ValT> ValT peek(int offs, Type<ValT> type) { return peek(offs).as(type); }

    public Val pop() { return items.remove(items.size() - 1); }

    public <ValT> ValT pop(Type<ValT> type) { return pop().as(type); }

    private ArrayList<Val> items = new ArrayList<>();
}
