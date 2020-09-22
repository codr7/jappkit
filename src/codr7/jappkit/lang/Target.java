package codr7.jappkit.lang;

import java.util.ArrayList;
import java.util.List;

public class Target {
    public <OpT extends Op> OpT emit(OpT op) {
        ops.add(op);
        return op;
    }

    public void eval(VM vm, Stack stack, int pc) {
        for (int i = pc; pc < ops.size(); pc = ops.get(pc).eval(vm, stack));
    }

    public int nops() { return ops.size(); }

    private ArrayList<Op> ops = new ArrayList<>();
}
