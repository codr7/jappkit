package codr7.jappkit.calc;

import codr7.jappkit.Fix;
import codr7.jappkit.type.LongType;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class CalcTest {
    @Test
    public void eval() {
        var c = new Calc();
        c.set("?", LongType.it, 21L);
        assertEquals(c.eval(new Reader("? + 7 * 3")).data, 42L);
    }

    @Test
    public void decimals() {
        var c = new Calc();
        c.set("?", LongType.it, 21L);
        assertEquals(c.eval(new Reader("? + 3.5 * 6")).data, Fix.make(42));
    }
}