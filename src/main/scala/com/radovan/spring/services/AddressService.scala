package com.radovan.spring.services

import com.radovan.spring.dto.AddressDto

trait AddressService {

  def getAddressById(addressId:Integer):AddressDto
  def createAddress(address:AddressDto):AddressDto
}
