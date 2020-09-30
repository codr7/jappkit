package codr7.jappkit.lang;

import codr7.jappkit.Stack;

import java.util.ArrayList;

public class Target {
    public Op emit(Op op) {
        ops.add(op);
        return op;
    }

    public void eval(VM vm, CallStack calls, Stack stack, int pc) {
        while ((pc = ops.get(pc).eval(vm, calls, stack)) != -1);
    }

    public int nops() { return ops.size(); }

    public void compile(VM vm, Env env) {
        ArrayList<Op> in = ops;
        ops = new ArrayList<>();
        for (Op op: in) { op.compile(vm, env,this); }
    }

    private ArrayList<Op> ops = new ArrayList<>();
}
