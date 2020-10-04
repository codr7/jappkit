package codr7.jappkit.lang.test;

import codr7.jappkit.lang.CallStack;
import codr7.jappkit.Stack;
import codr7.jappkit.lang.Env;
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
        new PushOp(vm).val(LongType.it, 42L);
        new StopOp(vm);

        Stack stack = new Stack();
        vm.eval(new CallStack(), stack, 0);
        Val val = stack.pop();
        assertEquals(val.type, LongType.it);
        assertEquals(val.data, 42L);
    }

    @Test
    public void fibrec() {
        VM vm = new VM();

        BranchOp skip = new BranchLtOp(vm).cond(2);
        new DecOp(vm);
        new CpOp(vm);
        new CallOp(vm).targetPc(0);
        new SwapOp(vm);
        new DecOp(vm);
        new CallOp(vm).targetPc(0);
        new AddOp(vm);
        skip.targetPc(vm.nops());
        new RetOp(vm);

        Stack stack = new Stack();
        stack.push(LongType.it, 20L);

        int startPc = vm.nops();
        new CallOp(vm).targetPc(0);
        new StopOp(vm);

        vm.eval(new CallStack(), stack, startPc);
        assertEquals(stack.pop(LongType.it).longValue(), 6765L);
    }

    @Test
    public void fibtail() {
        VM vm = new VM();

        BranchOp exit0 = new BranchEqOp(vm).cond(0).stackOffs(2);
        BranchOp exit1 = new BranchEqOp(vm).cond(1).stackOffs(2);
        new DecOp(vm).stackOffs = 2;
        new SwapOp(vm);
        new CpOp(vm).offs = 1;
        new AddOp(vm);
        new JmpOp(vm).targetPc(0);

        exit0.targetPc(vm.nops());
        new SwapOp(vm);

        exit1.targetPc(vm.nops());
        new DropOp(vm).offs(2).nitems(2);
        new RetOp(vm);

        Stack stack = new Stack();
        stack.push(LongType.it, 20L, 0L, 1L);

        int startPc = vm.nops();
        new CallOp(vm).targetPc(0);
        new StopOp(vm);

        vm.eval(new CallStack(), stack, startPc);
        assertEquals(stack.pop(LongType.it).longValue(), 6765L);
    }

    @Test
    public void fibiter() {
        VM vm = new VM();

        new PushOp(vm).val(LongType.it, 0L);
        new PushOp(vm).val(LongType.it, 1L);

        int loopPc = vm.nops();
        new SwapOp(vm);
        new CpOp(vm).offs(1);
        new AddOp(vm);
        new DecOp(vm).stackOffs(2);
        new BranchGtOp(vm).stackOffs(2).cond(0).targetPc(loopPc);

        new SwapOp(vm);
        new DropOp(vm).offs(2).nitems(2);
        new RetOp(vm);

        Stack stack = new Stack();
        stack.push(LongType.it, 20L);

        int startPc = vm.nops();
        new CallOp(vm).targetPc(0);
        new StopOp(vm);

        vm.eval(new CallStack(), stack, startPc);
        assertEquals(stack.pop(LongType.it).longValue(), 6765L);
    }
}