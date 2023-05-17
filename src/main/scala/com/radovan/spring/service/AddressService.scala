package com.radovan.spring.service

import com.radovan.spring.dto.AddressDto

trait AddressService {

  def getAddressById(addressId: Integer): AddressDto

  def createAddress(address: AddressDto): AddressDto

  def deleteAddress(addressId: Integer): Unit
}

