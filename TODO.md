* dom
  * add onclick event
  * add script support
  * add bookr ui
* db
  * bookr
    * add ChargeRule
      * start/end
      * from account to account
      * body
    * add charge from ModTest
      * add product field
    * add calc lang
      * parse numbers as fix
      * 1 + 2.0 * 3 + 4 * H
        * eval recursive descent
          * check next operator
    * add resource/product/item.label
      * add Item.label()
      *  generate on request when not set
    * finish item charge test
  * test nested tx
  * test parallel tx
* lang
* hospos
  * bookr+kalervos+servos+m4
  * module based