package codr7.jappkit.lang.tests;

import codr7.jappkit.lang.CallStack;
import codr7.jappkit.Stack;
import codr7.jappkit.lang.Target;
import codr7.jappkit.lang.VM;
import codr7.jappkit.Val;
import codr7.jappkit.lang.ops.*;
import codr7.jappkit.types.LongType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class VMTest {
    @Test
    public void eval() {
        VM vm = new VM();
        Target main = new Target();
        new PushOp(main, LongType.it, 42L);
        new StopOp(main);

        Stack stack = new Stack();
        main.eval(vm, new CallStack(), stack, 0);
        Val val = stack.pop();
        assertEquals(val.type, LongType.it);
        assertEquals(val.data, 42L);
    }

    @Test
    public void fibrec() {
        VM vm = new VM();
        Target main = new Target();

        BranchLessOp skip = new BranchLessOp(main, 2);
        new DecOp(main);
        new CpOp(main);
        new CallOp(main, 0);
        new SwapOp(main);
        new DecOp(main);
        new CallOp(main, 0);
        new AddOp(main);
        skip.mark(main);
        new RetOp(main);

        Stack stack = new Stack();
        stack.push(LongType.it, 20L);

        int startPc = main.nops();
        new CallOp(main, 0);
        new StopOp(main);

        main.eval(vm, new CallStack(), stack, startPc);
        assertEquals(stack.pop(LongType.it).longValue(), 6765L);
    }
}