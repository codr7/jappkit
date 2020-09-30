package codr7.jappkit.lang.test;

import codr7.jappkit.lang.CallStack;
import codr7.jappkit.Stack;
import codr7.jappkit.lang.Env;
import codr7.jappkit.lang.Target;
import codr7.jappkit.lang.VM;
import codr7.jappkit.Val;
import codr7.jappkit.lang.op.*;
import codr7.jappkit.type.LongType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class VMTest {
    @Test
    public void eval() {
        VM vm = new VM();
        Target main = new Target();
        new PushOp(main).val(LongType.it, 42L);
        new StopOp(main);

        Stack stack = new Stack();
        main.eval(vm, new CallStack(), stack, 0);
        Val val = stack.pop();
        assertEquals(val.type, LongType.it);
        assertEquals(val.data, 42L);
    }

    @Test
    public void compile() {
        VM vm = new VM();
        Target main = new Target();
        Val val = Val.make(LongType.it, 42L);

        new DefConstOp(main).key("foo").val(val);
        new GetConstOp(main).key("foo");
        new StopOp(main);
        main.compile(vm, new Env());

        Stack stack = new Stack();
        main.eval(vm, new CallStack(), stack, 0);
        assertEquals(stack.pop(), val);
    }

    @Test
    public void fibrec() {
        VM vm = new VM();
        Target main = new Target();

        BranchOp skip = new BranchLtOp(main).cond(2);
        new DecOp(main);
        new CpOp(main);
        new CallOp(main).targetPc(0);
        new SwapOp(main);
        new DecOp(main);
        new CallOp(main).targetPc(0);
        new AddOp(main);
        skip.targetPc(main);
        new RetOp(main);

        Stack stack = new Stack();
        stack.push(LongType.it, 20L);

        int startPc = main.nops();
        new CallOp(main).targetPc(0);
        new StopOp(main);

        main.eval(vm, new CallStack(), stack, startPc);
        assertEquals(stack.pop(LongType.it).longValue(), 6765L);
    }

    @Test
    public void fibtail() {
        VM vm = new VM();
        Target main = new Target();

        BranchOp exit0 = new BranchEqOp(main).cond(0).stackOffs(2);
        BranchOp exit1 = new BranchEqOp(main).cond(1).stackOffs(2);
        new DecOp(main).stackOffs = 2;
        new SwapOp(main);
        new CpOp(main).offs = 1;
        new AddOp(main);
        new JmpOp(main).targetPc(0);

        exit0.targetPc(main);
        new SwapOp(main);

        exit1.targetPc(main);
        new DropOp(main).offs(2).nitems(2);
        new RetOp(main);

        Stack stack = new Stack();
        stack.push(LongType.it, 20L, 0L, 1L);

        int startPc = main.nops();
        new CallOp(main).targetPc(0);
        new StopOp(main);

        main.eval(vm, new CallStack(), stack, startPc);
        assertEquals(stack.pop(LongType.it).longValue(), 6765L);
    }

    @Test
    public void fibiter() {
        VM vm = new VM();
        Target main = new Target();

        new PushOp(main).val(LongType.it, 0L);
        new PushOp(main).val(LongType.it, 1L);

        int loopPc = main.nops();
        new SwapOp(main);
        new CpOp(main).offs(1);
        new AddOp(main);
        new DecOp(main).stackOffs(2);
        new BranchGtOp(main).stackOffs(2).cond(0).targetPc(loopPc);

        new SwapOp(main);
        new DropOp(main).offs(2).nitems(2);
        new RetOp(main);

        Stack stack = new Stack();
        stack.push(LongType.it, 20L);

        int startPc = main.nops();
        new CallOp(main).targetPc(0);
        new StopOp(main);

        main.eval(vm, new CallStack(), stack, startPc);
        assertEquals(stack.pop(LongType.it).longValue(), 6765L);
    }
}