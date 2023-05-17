package com.radovan.spring.controller

import com.radovan.spring.exceptions.InvalidUserException
import com.radovan.spring.form.RegistrationForm
import com.radovan.spring.service.{AddressService, CustomerService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PostMapping}

import java.security.Principal
import java.util.Optional

//noinspection ScalaUnusedSymbol
@Controller
class MainController {

  @Autowired
  private val customerService: CustomerService = null

  @Autowired
  private val userService:UserService = null

  @Autowired
  private val addressService:AddressService = null

  @GetMapping(value = Array("/"))
  def indexPage = "index"

  @GetMapping(value = Array("/login"))
  def login = "fragments/login :: ajaxLoadedContent"

  @GetMapping(value = Array("/home"))
  def homePage = "fragments/homePage :: ajaxLoadedContent"

  @GetMapping(value = Array("/userRegistration"))
  def registration(map: ModelMap): String = {
    val tempForm = new RegistrationForm
    map.put("tempForm", tempForm)
    "fragments/registration :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/saveUser"))
  def createUser(@ModelAttribute("tempForm") tempForm: RegistrationForm): String = {
    customerService.registerCustomer(tempForm)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/registerComplete"))
  def registrationCompleted = "fragments/registration_completed :: ajaxLoadedContent"

  @GetMapping(value = Array("/registerFail"))
  def registrationFailed = "fragments/registration_failed :: ajaxLoadedContent"

  @GetMapping(value = Array("/loginErrorPage"))
  def logError(map: ModelMap): String = {
    map.put("alert", "Invalid username or password!")
    "fragments/login :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/loginPassConfirm"))
  def confirmLoginPass(principal: Principal): String = {
    val authPrincipal = Optional.ofNullable(principal)
    if (!authPrincipal.isPresent) {
      val error = new Error("Invalid user")
      throw new InvalidUserException(error)
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/loggedout"))
  def logout: String = {
    val context = SecurityContextHolder.getContext
    context.setAuthentication(null)
    SecurityContextHolder.clearContext()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @PreAuthorize(value = "hasAuthority('ROLE_USER')")
  @GetMapping(value = Array("/accountInfo"))
  def userAccountInfo(map: ModelMap): String = {
    val authUser = userService.getCurrentUser
    val customer = customerService.getCustomerByUserId(authUser.getId)
    val address = addressService.getAddressById(customer.getAddressId)
    map.put("authUser", authUser)
    map.put("address", address)
    "fragments/accountDetails :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/about"))
  def aboutPage: String = "fragments/about :: ajaxLoadedContent"
}

