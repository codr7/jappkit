* add TimeColumn
  * Instant
  * add Encoding.read/writeTime
* finish maxTime support in open()
* add Index.records() method like Table
* add delete test to table/index
* add rollback test to table/index
* add BooleanColumn
* add nested Tx support
** add Tx.parent
*** constructor arg
*** forward updates on commit if set 