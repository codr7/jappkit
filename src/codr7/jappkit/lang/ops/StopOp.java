package codr7.jappkit.lang.ops;

import codr7.jappkit.Type;
import codr7.jappkit.lang.*;

public class StopOp extends Op {
    public StopOp(Target target) {
        super(target);
        target.emit(this);
    }

    public int eval(VM vm, Stack stack) { return -1; }
}