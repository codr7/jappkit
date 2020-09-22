package codr7.jappkit.lang;

import codr7.jappkit.Stack;

import java.util.ArrayList;

public class Target {
    public <OpT extends Op> OpT emit(OpT op) {
        ops.add(op);
        return op;
    }

    public void eval(VM vm, CallStack calls, Stack stack, int pc) {
        while ((pc = ops.get(pc).eval(vm, calls, stack)) != -1);
    }

    public int nops() { return ops.size(); }

    private ArrayList<Op> ops = new ArrayList<>();
}
