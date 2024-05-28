package com.radovan.spring.controller

import com.radovan.spring.dto.AddressDto
import com.radovan.spring.services.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

@Controller
@RequestMapping(value = Array("/addresses"))
class AddressController {

  @Autowired
  private var addressService:AddressService = _

  @GetMapping(value = Array("/updateAddress/{addressId}"))
  def renderAddressForm(@PathVariable("addressId") addressId: Integer, map: ModelMap): String = {
    map.put("address", new AddressDto)
    map.put("currentAddress", addressService.getAddressById(addressId))
    "fragments/updateAddressForm :: fragmentContent"
  }

  @PostMapping(value = Array("/createAddress"))
  def createAddress(@ModelAttribute("address") address: AddressDto): String = {
    addressService.createAddress(address)
    "fragments/homePage :: fragmentContent"
  }

}
