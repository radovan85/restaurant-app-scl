package com.radovan.spring.controller

import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.services.{AddressService, CustomerService, UserService}
import com.radovan.spring.utils.RegistrationForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PostMapping}

import java.security.Principal

@Controller
class MainController {

  private var customerService:CustomerService = _
  private var userService:UserService = _
  private var addressService:AddressService = _

  @Autowired
  private def injectAll(customerService: CustomerService,userService: UserService,
                        addressService: AddressService):Unit = {
    this.customerService = customerService
    this.userService = userService
    this.addressService = addressService
  }

  @GetMapping(value = Array("/"))
  def indexPage = "index"

  @GetMapping(value = Array("/login"))
  def login = "fragments/login :: fragmentContent"

  @GetMapping(value = Array("/home"))
  def homePage = "fragments/homePage :: fragmentContent"

  @GetMapping(value = Array("/userRegistration"))
  def registration(map: ModelMap): String = {
    val tempForm = new RegistrationForm
    map.put("tempForm", tempForm)
    "fragments/registration :: fragmentContent"
  }

  @PostMapping(value = Array("/saveUser"))
  def createUser(@ModelAttribute("tempForm") tempForm: RegistrationForm): String = {
    customerService.registerCustomer(tempForm)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/registerComplete"))
  def registrationCompleted = "fragments/registration_completed :: fragmentContent"

  @GetMapping(value = Array("/registerFail"))
  def registrationFailed = "fragments/registration_failed :: fragmentContent"

  import org.springframework.ui.ModelMap
  import org.springframework.web.bind.annotation.GetMapping

  @GetMapping(value = Array("/loginErrorPage"))
  def logError(map: ModelMap): String = {
    map.put("alert", "Invalid username or password!")
    "fragments/login :: fragmentContent"
  }

  @PostMapping(value = Array("/loginPassConfirm"))
  def confirmLoginPass(principal: Principal): String = {
    val authPrincipalOption = Option(principal)
    authPrincipalOption match {
      case Some(_) => "fragments/homePage :: fragmentContent"
      case None => throw new InstanceUndefinedException(new Error("Invalid user"))
    }
  }

  @PostMapping(value = Array("/loggedout"))
  def logout: String = {
    val context = SecurityContextHolder.getContext
    context.setAuthentication(null)
    SecurityContextHolder.clearContext()
    "fragments/homePage :: fragmentContent"
  }

  @PreAuthorize(value = "hasAuthority('ROLE_USER')")
  @GetMapping(value = Array("/accountInfo"))
  def userAccountInfo(map: ModelMap): String = {
    val authUser = userService.getCurrentUser()
    val customer = customerService.getCurrentCustomer()
    val address = addressService.getAddressById(customer.getAddressId)
    map.put("authUser", authUser)
    map.put("address", address)
    "fragments/accountDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/about"))
  def aboutPage: String = "fragments/about :: fragmentContent"
}
