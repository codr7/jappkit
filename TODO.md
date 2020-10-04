* dom
  * add onclick event
  * add script support
  * add bookr ui
* db
  * bookr
    * add calc lang
      * parse numbers as fix
      * 1 + 2.0 * 3 + 4 * H
        * eval recursive descent
          * check next operator
    * add charge from ModTest
      * add product field
    * finish item charge test
    * add resource/product/item.label
      * add Item.label()
      *  generate on request when not set
  * test nested tx
  * test parallel tx
* lang
* hospos
  * bookr+kalervos+servos+m4
  * module based