package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CustomerDto
import com.radovan.spring.entity.{CartEntity, RoleEntity, UserEntity}
import com.radovan.spring.exceptions.{InstanceAlreadyExistsException, InstanceUndefinedException}
import com.radovan.spring.repository.{AddressRepository, CartRepository, CustomerRepository, RoleRepository, UserRepository}
import com.radovan.spring.services.{CustomerService, OrderService, UserService}
import com.radovan.spring.utils.RegistrationForm
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._
import scala.collection.mutable.ArrayBuffer

@Service
class CustomerServiceImpl extends CustomerService{

  private var customerRepository:CustomerRepository = _
  private var userRepository:UserRepository = _
  private var roleRepository:RoleRepository = _
  private var passwordEncoder:BCryptPasswordEncoder = _
  private var addressRepository:AddressRepository = _
  private var cartRepository:CartRepository = _
  private var tempConverter:TempConverter = _
  private var userService:UserService = _
  private var orderService:OrderService = _

  @Autowired
  private def injectAll(customerRepository: CustomerRepository,userRepository: UserRepository,roleRepository: RoleRepository,
                        passwordEncoder: BCryptPasswordEncoder,addressRepository: AddressRepository,cartRepository: CartRepository,
                        tempConverter: TempConverter,userService: UserService,orderService: OrderService):Unit = {
    this.customerRepository = customerRepository
    this.userRepository = userRepository
    this.roleRepository = roleRepository
    this.passwordEncoder = passwordEncoder
    this.addressRepository = addressRepository
    this.cartRepository = cartRepository
    this.tempConverter = tempConverter
    this.userService = userService
    this.orderService = orderService
  }



  @Transactional
  override def addCustomer(customer: CustomerDto): CustomerDto = {
    val storedCustomer = customerRepository.save(tempConverter.customerDtoToEntity(customer))
    tempConverter.customerEntityToDto(storedCustomer)
  }

  @Transactional(readOnly = true)
  override def getCustomerById(customerId: Integer): CustomerDto = {
    val customerEntity = customerRepository.findById(customerId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The customer has not been found!")))
    tempConverter.customerEntityToDto(customerEntity)
  }

  @Transactional(readOnly = true)
  override def getCustomerByUserId(userId: Integer): CustomerDto = {
    val customerOption = customerRepository.findByUserId(userId)
    customerOption match {
      case Some(customer) => tempConverter.customerEntityToDto(customer)
      case None => throw new InstanceUndefinedException(new Error("The customer has not been found!"))
    }
  }

  @Transactional(readOnly = true)
  override def listAll(): Array[CustomerDto] = {
    val allCustomers = customerRepository.findAll().asScala
    allCustomers.map(customerEntity => tempConverter.customerEntityToDto(customerEntity)).toArray
  }

  @Transactional
  override def registerCustomer(form: RegistrationForm): CustomerDto = {
    val user = form.getUser
    val userOption = userRepository.findByEmail(user.getEmail)
    userOption match {
      case Some(_) => throw new InstanceAlreadyExistsException(new Error("This email exists already!"))
      case None =>
    }

    val roleEntity = roleRepository.findByRole("ROLE_USER").getOrElse(null)
    user.setPassword(passwordEncoder.encode(user.getPassword))
    user.setEnabled(1.asInstanceOf[Short])
    val roles = new ArrayBuffer[RoleEntity]()
    roles += roleEntity
    val userEntity = tempConverter.userDtoToEntity(user)
    userEntity.setRoles(roles.asJava)
    val storedUser = userRepository.save(userEntity)
    val users = new ArrayBuffer[UserEntity]()
    users += storedUser
    roleEntity.setUsers(users.asJava)
    roleRepository.saveAndFlush(roleEntity)

    val storedAddress = addressRepository.save(tempConverter.addressDtoToEntity(form.getAddress))
    val cartEntity = new CartEntity
    cartEntity.setCartPrice(0f)
    val storedCart = cartRepository.save(cartEntity)

    val customer = form.getCustomer
    customer.setCartId(storedCart.getId)
    customer.setAddressId(storedAddress.getId)
    customer.setUserId(storedUser.getId)
    val storedCustomer = customerRepository.save(tempConverter.customerDtoToEntity(customer))

    storedCart.setCustomer(storedCustomer)
    cartRepository.saveAndFlush(storedCart)

    storedAddress.setCustomer(storedCustomer)
    addressRepository.saveAndFlush(storedAddress)

    tempConverter.customerEntityToDto(storedCustomer)
  }

  @Transactional(readOnly = true)
  override def getCustomerByCartId(cartId: Integer): CustomerDto = {
    val customerEntity = customerRepository.findByCartId(cartId)
      .getOrElse(throw new InstanceUndefinedException(new Error("The customer has not been found!")))
    tempConverter.customerEntityToDto(customerEntity)
  }

  @Transactional
  override def deleteCustomer(customerId: Integer): Unit = {
    val allOrders = orderService.listAllByCustomerId(customerId)
    allOrders.foreach(order => orderService.deleteOrder(order.getId))
    customerRepository.deleteById(customerId)
    customerRepository.flush()
  }

  @Transactional(readOnly = true)
  override def getCurrentCustomer(): CustomerDto = {
    val authUser = userService.getCurrentUser()
    getCustomerByUserId(authUser.getId)
  }
}
