package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.AddressDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repository.AddressRepository
import com.radovan.spring.services.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AddressServiceImpl extends AddressService {

  private var addressRepository:AddressRepository = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def injectAll(addressRepository: AddressRepository,tempConverter: TempConverter):Unit = {
    this.addressRepository = addressRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def getAddressById(addressId: Integer): AddressDto = {
    val addressEntity = addressRepository.findById(addressId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The address has not been found!")))
    tempConverter.addressEntityToDto(addressEntity)
  }

  @Transactional
  override def createAddress(address: AddressDto): AddressDto = {
    val storedAddress = addressRepository.save(tempConverter.addressDtoToEntity(address))
    tempConverter.addressEntityToDto(storedAddress)
  }
}
