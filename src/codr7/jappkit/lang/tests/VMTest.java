package codr7.jappkit.lang.tests;

import codr7.jappkit.lang.Stack;
import codr7.jappkit.lang.Target;
import codr7.jappkit.lang.VM;
import codr7.jappkit.lang.Val;
import codr7.jappkit.lang.ops.PushOp;
import codr7.jappkit.types.LongType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class VMTest {
    @Test
    public void eval() {
        VM vm = new VM();
        vm.debug = true;
        Target main = new Target();
        new PushOp(main, LongType.it, 42L);
        Stack stack = new Stack();
        main.eval(vm, stack, 0);
        Val val = stack.pop();
        assertEquals(val.type, LongType.it);
        assertEquals(val.data, 42L);
    }
}