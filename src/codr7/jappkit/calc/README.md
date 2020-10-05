# calc

### intro
This package implements an embedded calculator language with types and variables.

```
var c = new Calc();
c.set("?", LongType.it, 21L);
assertEquals(c.eval(new Reader("? + 7 * 3")).data, 42L);
```

### types
* Integers are parsed as `long`.
* Decimals and divisions are parsed as 4 decimal fixnums implemented using scaled `long`.

### operators
The language supports mixed mode arithmetics using the basic operators (`+-*/`).