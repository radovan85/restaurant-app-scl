package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class ProductDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var productName: String = _
  @BeanProperty var productPrice: Float = _
  @BeanProperty var description: String = _
  @BeanProperty var category: String = _
  @BeanProperty var categoryList: Array[String] = _
  @BeanProperty var imageUrl: String = _

  categoryList = Array("Breakfast","Lunch","Snack","Dinner","Drinks")
}
