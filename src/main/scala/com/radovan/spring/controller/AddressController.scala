package com.radovan.spring.controller

import com.radovan.spring.dto.AddressDto
import com.radovan.spring.service.AddressService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

@Controller
@RequestMapping(value = Array("/addresses"))
class AddressController {

  @Autowired
  private val addressService: AddressService = null

  @GetMapping(value = Array("/updateAddress/{addressId}"))
  def renderAddressForm(@PathVariable("addressId") addressId: Integer, map: ModelMap): String = {
    val address = new AddressDto
    val currentAddress = addressService.getAddressById(addressId)
    map.put("address", address)
    map.put("currentAddress", currentAddress)
    "fragments/updateAddressForm :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/createAddress"))
  def createAddress(@ModelAttribute("address") address: AddressDto): String = {
    addressService.createAddress(address)
    "fragments/homePage :: ajaxLoadedContent"
  }
}

