package com.radovan.spring.service.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderAddressDto
import com.radovan.spring.repository.OrderAddressRepository
import com.radovan.spring.service.OrderAddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional
class OrderAddressServiceImpl extends OrderAddressService {

  @Autowired
  private val addressRepository:OrderAddressRepository = null

  @Autowired
  private val tempConverter:TempConverter = null

  override def getAddressById(addressId: Integer): OrderAddressDto = {
    var returnValue:OrderAddressDto = null
    val addressOpt = addressRepository.findById(addressId)
    if (addressOpt.isPresent) returnValue = tempConverter.orderAddressEntityToDto(addressOpt.get)
    returnValue
  }

  override def deleteAddress(addressId: Integer): Unit = {
    addressRepository.deleteById(addressId)
    addressRepository.flush()
  }
}

