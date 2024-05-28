package com.radovan.spring.converter

import com.radovan.spring.dto.{AddressDto, CartDto, CartItemDto, CustomerDto, OrderAddressDto, OrderDto, OrderItemDto, ProductDto, RoleDto, UserDto}
import com.radovan.spring.entity.{AddressEntity, CartEntity, CartItemEntity, CustomerEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, ProductEntity, RoleEntity, UserEntity}
import com.radovan.spring.repository.{AddressRepository, CartItemRepository, CartRepository, CustomerRepository, OrderAddressRepository, OrderItemRepository, OrderRepository, ProductRepository, RoleRepository, UserRepository}
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.text.DecimalFormat
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._

@Component
class TempConverter {

  private var mapper:ModelMapper = _
  private var roleRepository:RoleRepository = _
  private var userRepository:UserRepository = _
  private var customerRepository:CustomerRepository = _
  private var cartItemRepository:CartItemRepository = _
  private var cartRepository:CartRepository = _
  private var productRepository:ProductRepository = _
  private var addressRepository:AddressRepository = _
  private var orderRepository:OrderRepository = _
  private var orderItemRepository:OrderItemRepository = _
  private var orderAddressRepository:OrderAddressRepository = _
  private val decfor = new DecimalFormat("0.00")
  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
  private val zoneId = ZoneId.of("Europe/Belgrade")

  @Autowired
  def injectAll(roleRepository: RoleRepository,userRepository: UserRepository,customerRepository: CustomerRepository,
                cartItemRepository: CartItemRepository,cartRepository: CartRepository,productRepository: ProductRepository,
                addressRepository: AddressRepository,orderRepository: OrderRepository,orderItemRepository: OrderItemRepository,
                mapper: ModelMapper,orderAddressRepository: OrderAddressRepository): Unit ={

    this.roleRepository = roleRepository
    this.userRepository = userRepository
    this.customerRepository = customerRepository
    this.cartItemRepository = cartItemRepository
    this.cartRepository = cartRepository
    this.productRepository = productRepository
    this.addressRepository = addressRepository
    this.orderRepository = orderRepository
    this.orderItemRepository = orderItemRepository
    this.mapper = mapper
    this.orderAddressRepository = orderAddressRepository
  }

  def cartEntityToDto(cart:CartEntity):CartDto = {
    val returnValue = mapper.map(cart, classOf[CartDto])

    val customerOption = Option(cart.getCustomer)
    customerOption match {
      case Some(customer) =>
        returnValue.setCustomerId(customer.getId)
      case None =>
    }

    val cartItemsOption = Option(cart.getCartItems)
    val cartItemsIds = new ArrayBuffer[Integer]()
    cartItemsOption match {
      case Some(cartItems) =>
        cartItems.forEach(item => cartItemsIds += item.getId)
      case None =>
    }

    returnValue.setCartItemsIds(cartItemsIds.toArray)
    returnValue.setCartPrice(decfor.format(returnValue.getCartPrice).toFloat)
    returnValue
  }

  def cartDtoToEntity(cart:CartDto):CartEntity = {
    val returnValue = mapper.map(cart,classOf[CartEntity])

    val customerIdOption = Option(cart.getCustomerId)
    customerIdOption match {
      case Some(customerId) =>
        val customerEntity = customerRepository.findById(customerId).orElse(null)
        if(customerEntity != null){
          returnValue.setCustomer(customerEntity)
        }
      case None =>
    }

    val cartItems = new ArrayBuffer[CartItemEntity]()
    val cartItemsIdsOption = Option(cart.getCartItemsIds)
    cartItemsIdsOption match {
      case Some(cartItemsIds) =>
        cartItemsIds.foreach(itemId => {
          val cartItemEntity = cartItemRepository.findById(itemId).orElse(null)
          if(cartItemEntity!=null){
            cartItems += cartItemEntity
          }
        })
      case None =>
    }

    returnValue.setCartItems(cartItems.asJava)
    returnValue.setCartPrice(decfor.format(returnValue.getCartPrice).toFloat)
    returnValue
  }

  def cartItemEntityToDto(cartItem:CartItemEntity):CartItemDto = {
    val returnValue = mapper.map(cartItem, classOf[CartItemDto])
    val cartOption = Option(cartItem.getCart)
    if(cartOption.isDefined){
      returnValue.setCartId(cartOption.get.getId)
    }

    val productOption = Option(cartItem.getProduct)
    productOption match {
      case Some(product) =>
        returnValue.setProductId(product.getId)
        returnValue.setPrice(decfor.format(product.getProductPrice * returnValue.getQuantity).toFloat)
      case None =>
    }

    returnValue
  }

  def cartItemDtoToEntity(cartItem:CartItemDto):CartItemEntity = {
    val returnValue = mapper.map(cartItem, classOf[CartItemEntity])
    val cartIdOption = Option(cartItem.getCartId)
    cartIdOption match {
      case Some(cartId) =>
        val cartEntity = cartRepository.findById(cartId).orElse(null)
        if(cartEntity!=null){
          returnValue.setCart(cartEntity)
        }
      case None =>
    }

    val productIdOption = Option(cartItem.getProductId)
    productIdOption match {
      case Some(productId) =>
        val productEntity = productRepository.findById(productId).orElse(null)
        if(productEntity!=null){
          returnValue.setProduct(productEntity)
          returnValue.setPrice(decfor.format(productEntity.getProductPrice * returnValue.getQuantity).toFloat)
        }
      case None =>
    }

    returnValue
  }

  def addressEntityToDto(address:AddressEntity):AddressDto = {
    val returnValue = mapper.map(address, classOf[AddressDto])
    val customerOption = Option(address.getCustomer)
    if(customerOption.isDefined){
      returnValue.setCustomerId(customerOption.get.getId)
    }

    returnValue
  }

  def addressDtoToEntity(address:AddressDto):AddressEntity = {
    val returnValue = mapper.map(address, classOf[AddressEntity])
    val customerIdOption = Option(address.getCustomerId)
    customerIdOption match {
      case Some(customerId) =>
        val customerEntity = customerRepository.findById(customerId).orElse(null)
        if(customerEntity!=null){
          returnValue.setCustomer(customerEntity)
        }
      case None =>
    }

    returnValue
  }

  def customerEntityToDto(customer:CustomerEntity):CustomerDto = {
    val returnValue = mapper.map(customer, classOf[CustomerDto])
    val addressOption = Option(customer.getAddress)
    if(addressOption.isDefined){
      returnValue.setAddressId(addressOption.get.getId)
    }

    val userOption = Option(customer.getUser)
    if(userOption.isDefined){
      returnValue.setUserId(userOption.get.getId)
    }

    val cartOption = Option(customer.getCart)
    if(cartOption.isDefined){
      returnValue.setCartId(cartOption.get.getId)
    }

    returnValue
  }

  def customerDtoToEntity(customer:CustomerDto):CustomerEntity = {
    val returnValue = mapper.map(customer, classOf[CustomerEntity])
    val addressIdOption = Option(customer.getAddressId)
    addressIdOption match {
      case Some(addressId) =>
        val addressEntity = addressRepository.findById(addressId).orElse(null)
        if(addressEntity!=null){
          returnValue.setAddress(addressEntity)
        }
      case None =>
    }

    val userIdOption = Option(customer.getUserId)
    userIdOption match {
      case Some(userId) =>
        val userEntity = userRepository.findById(userId).orElse(null)
        if(userEntity!=null){
          returnValue.setUser(userEntity)
        }
      case None =>
    }

    val cartIdOption = Option(customer.getCartId)
    cartIdOption match {
      case Some(cartId) =>
        val cartEntity = cartRepository.findById(cartId).orElse(null)
        if(cartEntity!=null){
          returnValue.setCart(cartEntity)
        }
      case None =>
    }

    returnValue
  }

  def orderAddressEntityToDto(address:OrderAddressEntity): OrderAddressDto ={
    val returnValue = mapper.map(address,classOf[OrderAddressDto])
    val orderOption = Option(address.getOrder)
    orderOption match {
      case Some(order) => returnValue.setOrderId(order.getId)
      case None =>
    }

    returnValue
  }

  def orderAddressDtoToEntity(address:OrderAddressDto):OrderAddressEntity = {
    val returnValue = mapper.map(address, classOf[OrderAddressEntity])
    val orderIdOption = Option(address.getOrderId)
    orderIdOption match {
      case Some(orderId) =>
        val orderEntity = orderRepository.findById(orderId).orElse(null)
        if(orderEntity!=null){
          returnValue.setOrder(orderEntity)
        }
      case None =>
    }

    returnValue
  }

  def orderEntityToDto(order:OrderEntity): OrderDto ={
    val returnValue = mapper.map(order, classOf[OrderDto])
    val cartOption = Option(order.getCart)
    if(cartOption.isDefined){
      returnValue.setCartId(cartOption.get.getId)
    }

    val createdAtOption = Option(order.getCreatedAt)
    createdAtOption match {
      case Some(createdAt) =>
        val createdAtStr = createdAt.toLocalDateTime.atZone(zoneId).format(formatter)
        returnValue.setCreatedAt(createdAtStr)
      case None =>
    }

    val orderedItemsOption = Option(order.getOrderedItems)
    val orderedItemsIds = new ArrayBuffer[Integer]()
    orderedItemsOption match {
      case Some(orderedItems) =>
        orderedItems.forEach(itemEntity => orderedItemsIds += itemEntity.getId)
      case None => ???
    }
    returnValue.setOrderedItemsIds(orderedItemsIds.toArray)

    val addressOption = Option(order.getAddress)
    if(addressOption.isDefined){
      returnValue.setAddressId(addressOption.get.getId)
    }

    returnValue
  }

  def orderDtoToEntity(order:OrderDto):OrderEntity = {
    val returnValue = mapper.map(order, classOf[OrderEntity])

    val cartIdOption = Option(order.getCartId)
    cartIdOption match {
      case Some(cartId) =>
        val cartEntity = cartRepository.findById(cartId).orElse(null)
        if(cartEntity!=null){
          returnValue.setCart(cartEntity)
        }
      case None =>
    }

    val orderedItemsIdsOption = Option(order.getOrderedItemsIds)
    val orderedItems = new ArrayBuffer[OrderItemEntity]()
    orderedItemsIdsOption match {
      case Some(orderedItemsIds) =>
        orderedItemsIds.foreach(itemId => {
          val itemEntity = orderItemRepository.findById(itemId).orElse(null)
          if(itemEntity!=null){
            orderedItems += itemEntity
          }
        })
      case None =>
    }
    returnValue.setOrderedItems(orderedItems.asJava)

    val addressIdOption = Option(order.getAddressId)
    addressIdOption match {
      case Some(addressId) =>
        val addressEntity:OrderAddressEntity = orderAddressRepository.findById(addressId).orElse(null)
        if(addressEntity!=null){
          returnValue.setAddress(addressEntity)
        }
      case None =>
    }
    returnValue
  }

  def orderItemEntityToDto(orderItem:OrderItemEntity):OrderItemDto = {
    val returnValue = mapper.map(orderItem, classOf[OrderItemDto])
    val orderOption = Option(orderItem.getOrder)
    if(orderOption.isDefined){
      returnValue.setOrderId(orderOption.get.getId)
    }

    returnValue
  }

  def orderItemDtoToEntity(orderItem:OrderItemDto):OrderItemEntity = {
    val returnValue = mapper.map(orderItem, classOf[OrderItemEntity])
    val orderIdOption = Option(orderItem.getOrderId)
    orderIdOption match {
      case Some(orderId) =>
        val orderEntity = orderRepository.findById(orderId).orElse(null)
        if(orderEntity!=null){
          returnValue.setOrder(orderEntity)
        }
      case None =>
    }

    returnValue
  }

  def productEntityToDto(product:ProductEntity):ProductDto = {
    var returnValue = mapper.map(product, classOf[ProductDto])
    returnValue.setProductPrice(decfor.format(returnValue.getProductPrice).toFloat)
    returnValue
  }

  def productDtoToEntity(product:ProductDto):ProductEntity = {
    var returnValue = mapper.map(product, classOf[ProductEntity])
    returnValue.setProductPrice(decfor.format(returnValue.getProductPrice).toFloat)
    returnValue
  }

  def roleEntityToDto(role: RoleEntity): RoleDto = {
    val returnValue = mapper.map(role, classOf[RoleDto])
    val usersOpt = Option(role.getUsers.asScala)
    val usersIds = new ArrayBuffer[Integer]()
    if (usersOpt.isDefined) {
      usersOpt.get.foreach(userEntity => usersIds += userEntity.getId)
    }

    returnValue.setUsersIds(usersIds.toArray)
    returnValue
  }

   def roleDtoToEntity(role: RoleDto): RoleEntity = {
    val returnValue = mapper.map(role, classOf[RoleEntity])
    val usersIdsOpt = Option(role.getUsersIds)
    val users = new ArrayBuffer[UserEntity]()
    usersIdsOpt match {
      case Some(usersIds) =>
        usersIds.foreach(userId => {
          val userEntity = userRepository.findById(userId).orElse(null)
          if (userEntity != null) {
            users += userEntity
          }
        })
      case None =>
    }

    returnValue.setUsers(users.asJava)
    returnValue
  }

  def userEntityToDto(user: UserEntity): UserDto = {
    val returnValue = mapper.map(user, classOf[UserDto])
    val rolesOpt = Option(user.getRoles.asScala)
    val rolesIds = new ArrayBuffer[Integer]()
    if (rolesOpt.isDefined) {
      rolesOpt.get.foreach(role => rolesIds += role.getId)
    }

    val enabledOpt = Option(user.getEnabled)
    enabledOpt match {
      case Some(enabled) => returnValue.setEnabled(enabled.asInstanceOf[Short])
      case None =>
    }

    returnValue.setRolesIds(rolesIds.toArray)
    returnValue
  }

   def userDtoToEntity(user: UserDto): UserEntity = {
    val returnValue = mapper.map(user, classOf[UserEntity])
    val rolesIdsOpt = Option(user.getRolesIds)
    val roles = new ArrayBuffer[RoleEntity]()
    rolesIdsOpt match {
      case Some(rolesIds) =>
        rolesIds.foreach(roleId => {
          val roleEntity = roleRepository.findById(roleId).orElse(null)
          if (roleEntity != null) {
            roles += roleEntity
          }
        })
      case None =>
    }

    val enabledOpt = Option(user.getEnabled)
    enabledOpt match {
      case Some(enabled) => returnValue.setEnabled(enabled.asInstanceOf[Byte])
      case None =>
    }

    returnValue.setRoles(roles.asJava)
    returnValue
  }

  def addressToOrderAddress(address:AddressDto):OrderAddressDto = {
    mapper.map(address, classOf[OrderAddressDto])
  }

  def cartItemToOrderItem(cartItem:CartItemDto):OrderItemDto = {
    val returnValue = mapper.map(cartItem, classOf[OrderItemDto])
    val productIdOption = Option(cartItem.getProductId)
    productIdOption match {
      case Some(productId) =>
        val productEntity = productRepository.findById(productId).orElse(null)
        if(productEntity!=null){
          returnValue.setProductPrice(productEntity.getProductPrice)
          returnValue.setProductName(productEntity.getProductName)
        }
      case None =>
    }

    returnValue
  }

}
