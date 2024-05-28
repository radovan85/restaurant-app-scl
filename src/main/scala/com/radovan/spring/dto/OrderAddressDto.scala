package com.radovan.spring.dto

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class OrderAddressDto extends Serializable {

  @BeanProperty var id: Integer = _
  @BeanProperty var address: String = _
  @BeanProperty var city: String = _
  @BeanProperty var postcode: String = _
  @BeanProperty var orderId: Integer = _
}
