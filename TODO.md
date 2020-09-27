* dom
  * add opts arg to write
    * add WriteOpts enum
      * add FORMAT
    * skip newlines unless set
    * add depth arg
      * print spaces
    * simplify tests
      * keep one to test formatting
  * add onclick event
  * add script support
  * add bookr demo
* db
  * add Mod
  ** reflect on col name
  * add Ref<ModT>
    * id & ref like RecProxy
    * add RefCol
  * test nested tx
  * test parallel tx
  * add bookr demo
* lang