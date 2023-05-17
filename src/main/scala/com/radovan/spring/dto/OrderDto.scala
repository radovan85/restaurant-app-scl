package com.radovan.spring.dto

import java.util
import java.sql.Timestamp
import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderDto extends Serializable {

  @BeanProperty var orderId:Integer = _
  @BeanProperty var customerId:Integer = _
  @BeanProperty var addressId:Integer = _
  @BeanProperty var orderItemsIds:util.List[Integer] = _
  @BeanProperty var price:Float = _
  @BeanProperty var createdAt:Timestamp = _
  @BeanProperty var createdAtStr:String = _

}
