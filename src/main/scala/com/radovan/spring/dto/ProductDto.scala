package com.radovan.spring.dto

import java.util
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class ProductDto() extends Serializable {

  @BeanProperty var productId:Integer = _
  @BeanProperty var productName:String = _
  @BeanProperty var productPrice:Float = _
  @BeanProperty var description:String = _
  @BeanProperty var category:String = _
  @BeanProperty var categoryList:util.List[String] = _
  @BeanProperty var imageUrl:String = _

  categoryList = new util.ArrayList[String]
  categoryList.add("Breakfast")
  categoryList.add("Lunch")
  categoryList.add("Snack")
  categoryList.add("Dinner")
  categoryList.add("Drinks")
}

