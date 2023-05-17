package com.radovan.spring.form

import com.radovan.spring.dto.{AddressDto, CustomerDto, UserDto}

import scala.beans.BeanProperty

@SerialVersionUID(1L)
class RegistrationForm extends Serializable {

  @BeanProperty var user:UserDto = _
  @BeanProperty var customer:CustomerDto = _
  @BeanProperty var address:AddressDto = _
}
