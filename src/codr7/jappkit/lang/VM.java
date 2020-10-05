package codr7.jappkit.lang;

import codr7.jappkit.Stack;

import java.util.ArrayList;

public class VM {
    public boolean debug = false;

    public Op emit(Op op) {
        ops.add(op);
        return op;
    }

    public void eval(CallStack calls, Stack stack, int pc) {
        while ((pc = ops.get(pc).eval(this, calls, stack)) != -1);
    }

    public int nops() { return ops.size(); }

    public void clear() { ops.clear(); }

    private ArrayList<Op> ops = new ArrayList<>();
}
