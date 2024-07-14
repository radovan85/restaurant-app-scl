package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class CartItemDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var price: Float = _
  @BeanProperty var quantity: Integer = _
  @BeanProperty var hotnessLevel: String = _
  @BeanProperty var cartId: Integer = _
  @BeanProperty var productId: Integer = _
  @BeanProperty var hotnessLevelList: Array[String] = _

  hotnessLevelList = Array("Extremely cold","Cold","Warmish","Warm","Hot","Extremely hot")
}
