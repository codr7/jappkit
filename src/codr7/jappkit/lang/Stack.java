package codr7.jappkit.lang;

import codr7.jappkit.Val;

import java.util.ArrayList;

public class Stack {
    public Val push(Val it) {
        items.add(it);
        return it;
    }

    public Val pop() { return items.remove(items.size()-1); }

    private ArrayList<Val> items = new ArrayList<>();
}
