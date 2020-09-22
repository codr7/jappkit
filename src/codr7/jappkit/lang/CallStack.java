package codr7.jappkit.lang;

import java.util.ArrayList;

public class CallStack {
    public void push(int pc) { items.add(pc); }

    public int peek() { return items.get(items.size()-1); }

    public int pop() { return items.remove(items.size()-1); }

    private ArrayList<Integer> items = new ArrayList<>();
}
