package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderItemDto extends Serializable {

  @BeanProperty var orderItemId:Integer = _
  @BeanProperty var price:Float = _
  @BeanProperty var quantity:Integer = _
  @BeanProperty var hotnessLevel:String = _
  @BeanProperty var productName:String = _
  @BeanProperty var productPrice:Float = _
  @BeanProperty var orderId:Integer = _

}

