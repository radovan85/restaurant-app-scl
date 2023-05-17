package com.radovan.spring.converter

import com.radovan.spring.dto.{AddressDto, CartDto, CartItemDto, CustomerDto, OrderAddressDto, OrderDto, OrderItemDto, ProductDto, RoleDto, UserDto}
import com.radovan.spring.entity.{AddressEntity, CartEntity, CartItemEntity, CustomerEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, ProductEntity, RoleEntity, UserEntity}
import com.radovan.spring.repository.{AddressRepository, CartItemRepository, CartRepository, CustomerRepository, OrderAddressRepository, OrderItemRepository, OrderRepository, ProductRepository, RoleRepository, UserRepository}
import org.modelmapper.ModelMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

import java.text.DecimalFormat
import java.time.format.DateTimeFormatter
import java.util
import java.util.Optional

@Component
class TempConverter {

  @Autowired
  private val mapper:ModelMapper = null

  @Autowired
  private val cartRepository:CartRepository = null

  @Autowired
  private val customerRepository:CustomerRepository = null

  @Autowired
  private val cartItemRepository:CartItemRepository = null

  @Autowired
  private val productRepository:ProductRepository = null

  @Autowired
  private val userRepository:UserRepository = null

  @Autowired
  private val orderRepository:OrderRepository = null

  @Autowired
  private val orderItemRepository:OrderItemRepository = null

  @Autowired
  private val roleRepository:RoleRepository = null

  @Autowired
  private val addressRepository:AddressRepository = null

  @Autowired
  private val orderAddressRepository: OrderAddressRepository = null

  private val decfor = new DecimalFormat("0.00")

  private val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")

  def cartEntityToDto(cartEntity: CartEntity): CartDto = {
    val returnValue = mapper.map(cartEntity, classOf[CartDto])
    val customerOpt = Optional.ofNullable(cartEntity.getCustomer)
    if (customerOpt.isPresent) {
      val customerEntity = customerOpt.get
      returnValue.setCustomerId(customerEntity.getCustomerId)
    }
    val cartItemsOpt = Optional.ofNullable(cartEntity.getCartItems)
    val cartItemsIds = new util.ArrayList[Integer]
    if (!cartItemsOpt.isEmpty) {
      val cartItems = cartItemsOpt.get
      cartItems.forEach((item: CartItemEntity) => {
        def foo(item: CartItemEntity) = cartItemsIds.add(item.getCartItemId)

        foo(item)
      })
    }
    returnValue.setCartItemsIds(cartItemsIds)
    returnValue
  }

  def cartDtoToEntity(cartDto: CartDto): CartEntity = {
    val returnValue = mapper.map(cartDto, classOf[CartEntity])
    val customerIdOpt = Optional.ofNullable(cartDto.getCustomerId)
    if (customerIdOpt.isPresent) {
      val customerId = customerIdOpt.get
      val customer = customerRepository.findById(customerId).get
      returnValue.setCustomer(customer)
    }
    val cartItemsIdsOpt = Optional.ofNullable(cartDto.getCartItemsIds)
    val cartItems = new util.ArrayList[CartItemEntity]
    if (!cartItemsIdsOpt.isEmpty) {
      val cartItemsIds = cartItemsIdsOpt.get
      cartItemsIds.forEach((itemId: Integer) => {
        def foo(itemId: Integer) = {
          val itemEntity = cartItemRepository.findById(itemId).get
          cartItems.add(itemEntity)
        }

        foo(itemId)
      })
    }
    returnValue.setCartItems(cartItems)
    returnValue
  }

  def cartItemEntityToDto(itemEntity: CartItemEntity): CartItemDto = {
    val returnValue = mapper.map(itemEntity, classOf[CartItemDto])
    val cartOpt = Optional.ofNullable(itemEntity.getCart)
    if (cartOpt.isPresent) returnValue.setCartId(cartOpt.get.getCartId)
    val productOpt = Optional.ofNullable(itemEntity.getProduct)
    if (productOpt.isPresent) returnValue.setProductId(productOpt.get.getProductId)
    returnValue
  }

  def cartItemDtoToEntity(itemDto: CartItemDto): CartItemEntity = {
    val returnValue = mapper.map(itemDto, classOf[CartItemEntity])
    val cartIdOpt = Optional.ofNullable(itemDto.getCartId)
    if (cartIdOpt.isPresent) {
      val cartId = cartIdOpt.get
      val cartEntity = cartRepository.findById(cartId).get
      returnValue.setCart(cartEntity)
    }
    val productIdOpt = Optional.ofNullable(itemDto.getProductId)
    if (productIdOpt.isPresent) {
      val productId = productIdOpt.get
      val productEntity = productRepository.findById(productId).get
      returnValue.setProduct(productEntity)
    }
    returnValue
  }

  def customerEntityToDto(customerEntity: CustomerEntity): CustomerDto = {
    val returnValue = mapper.map(customerEntity, classOf[CustomerDto])
    val userOpt = Optional.ofNullable(customerEntity.getUser)
    if (userOpt.isPresent) returnValue.setUserId(userOpt.get.getId)
    val ordersOpt = Optional.ofNullable(customerEntity.getOrders)
    val ordersIds = new util.ArrayList[Integer]
    if (!ordersOpt.isEmpty) {
      val orders = ordersOpt.get
      orders.forEach((orderEntity: OrderEntity) => {
        def foo(orderEntity: OrderEntity) = ordersIds.add(orderEntity.getOrderId)

        foo(orderEntity)
      })
    }
    returnValue.setOrdersIds(ordersIds)
    val cartOpt = Optional.ofNullable(customerEntity.getCart)
    if (cartOpt.isPresent) returnValue.setCartId(cartOpt.get.getCartId)
    val addressOpt = Optional.ofNullable(customerEntity.getAddress)
    if (addressOpt.isPresent) returnValue.setAddressId(addressOpt.get.getAddressId)
    returnValue
  }

  def customerDtoToEntity(customer: CustomerDto): CustomerEntity = {
    val returnValue = mapper.map(customer, classOf[CustomerEntity])
    val userIdOpt = Optional.ofNullable(customer.getUserId)
    if (userIdOpt.isPresent) {
      val userId = userIdOpt.get
      val userEntity = userRepository.findById(userId).get
      returnValue.setUser(userEntity)
    }
    val ordersIdsOpt = Optional.ofNullable(customer.getOrdersIds)
    val orders = new util.ArrayList[OrderEntity]
    if (!ordersIdsOpt.isEmpty) {
      val ordersIds = ordersIdsOpt.get
      ordersIds.forEach((orderId: Integer) => {
        def foo(orderId: Integer) = {
          val orderEntity = orderRepository.findById(orderId).get
          orders.add(orderEntity)
        }

        foo(orderId)
      })
    }
    returnValue.setOrders(orders)
    val cartIdOpt = Optional.ofNullable(customer.getCartId)
    if (cartIdOpt.isPresent) {
      val cartId = cartIdOpt.get
      val cartEntity = cartRepository.findById(cartId).get
      returnValue.setCart(cartEntity)
    }
    val addressIdOpt = Optional.ofNullable(customer.getAddressId)
    if (addressIdOpt.isPresent) {
      val addressId = addressIdOpt.get
      val address = addressRepository.findById(addressId).get
      returnValue.setAddress(address)
    }
    returnValue
  }

  def orderEntityToDto(orderEntity: OrderEntity): OrderDto = {
    val returnValue = mapper.map(orderEntity, classOf[OrderDto])
    val customerOpt = Optional.ofNullable(orderEntity.getCustomer)
    if (customerOpt.isPresent) returnValue.setCustomerId(customerOpt.get.getCustomerId)
    val addressOpt = Optional.ofNullable(orderEntity.getAddress)
    if (addressOpt.isPresent) returnValue.setAddressId(addressOpt.get.getOrderAddressId)
    val orderItemsOpt = Optional.ofNullable(orderEntity.getOrderItems)
    val orderItemsIds = new util.ArrayList[Integer]
    if (!orderItemsOpt.isEmpty) {
      val orderItems = orderItemsOpt.get
      orderItems.forEach((itemEntity: OrderItemEntity) => {
        def foo(itemEntity: OrderItemEntity) = orderItemsIds.add(itemEntity.getOrderItemId)

        foo(itemEntity)
      })
    }
    val createdAtOpt = Optional.ofNullable(orderEntity.getCreatedAt)
    if (createdAtOpt.isPresent) {
      val createdAtStr = createdAtOpt.get.toLocalDateTime.format(formatter)
      returnValue.setCreatedAtStr(createdAtStr)
    }
    returnValue.setOrderItemsIds(orderItemsIds)
    returnValue
  }

  def orderDtoToEntity(order: OrderDto): OrderEntity = {
    val returnValue = mapper.map(order, classOf[OrderEntity])
    val customerIdOpt = Optional.ofNullable(order.getCustomerId)
    if (customerIdOpt.isPresent) {
      val customerId = customerIdOpt.get
      val customerEntity = customerRepository.findById(customerId).get
      returnValue.setCustomer(customerEntity)
    }
    val addressIdOpt = Optional.ofNullable(order.getAddressId)
    if (addressIdOpt.isPresent) {
      val addressId = addressIdOpt.get
      val addressEntity = orderAddressRepository.findById(addressId).get
      returnValue.setAddress(addressEntity)
    }
    val orderItemsIdsOpt = Optional.ofNullable(order.getOrderItemsIds)
    val orderItems = new util.ArrayList[OrderItemEntity]
    if (!orderItemsIdsOpt.isEmpty) {
      val orderItemsIds = orderItemsIdsOpt.get
      orderItemsIds.forEach((itemId: Integer) => {
        def foo(itemId: Integer) = {
          val itemEntity = orderItemRepository.findById(itemId).get
          orderItems.add(itemEntity)
        }

        foo(itemId)
      })
    }
    returnValue.setOrderItems(orderItems)
    returnValue
  }

  def orderItemEntityToDto(itemEntity: OrderItemEntity): OrderItemDto = {
    val returnValue = mapper.map(itemEntity, classOf[OrderItemDto])
    val orderOpt = Optional.ofNullable(itemEntity.getOrder)
    if (orderOpt.isPresent) returnValue.setOrderId(orderOpt.get.getOrderId)
    returnValue
  }

  def orderItemDtoToEntity(itemDto: OrderItemDto): OrderItemEntity = {
    val returnValue = mapper.map(itemDto, classOf[OrderItemEntity])
    val orderIdOpt = Optional.ofNullable(itemDto.getOrderId)
    if (orderIdOpt.isPresent) {
      val orderId = orderIdOpt.get
      val orderEntity = orderRepository.findById(orderId).get
      returnValue.setOrder(orderEntity)
    }
    returnValue
  }

  def productEntityToDto(productEntity: ProductEntity): ProductDto = {
    val returnValue = mapper.map(productEntity, classOf[ProductDto])
    val price = decfor.format(returnValue.getProductPrice).toFloat
    returnValue.setProductPrice(price)
    returnValue
  }

  def productDtoToEntity(product: ProductDto): ProductEntity = {
    val returnValue = mapper.map(product, classOf[ProductEntity])
    val price = decfor.format(returnValue.getProductPrice).toFloat
    returnValue.setProductPrice(price)
    returnValue
  }

  def userEntityToDto(userEntity: UserEntity): UserDto = {
    val returnValue = mapper.map(userEntity, classOf[UserDto])
    returnValue.setEnabled(userEntity.getEnabled)
    val rolesOpt = Optional.ofNullable(userEntity.getRoles)
    val rolesIds = new util.ArrayList[Integer]
    if (!rolesOpt.isEmpty) rolesOpt.get.forEach((roleEntity: RoleEntity) => {
      def foo(roleEntity: RoleEntity) = rolesIds.add(roleEntity.getId)

      foo(roleEntity)
    })
    returnValue.setRolesIds(rolesIds)
    returnValue
  }

  def userDtoToEntity(userDto: UserDto): UserEntity = {
    val returnValue = mapper.map(userDto, classOf[UserEntity])
    val roles = new util.ArrayList[RoleEntity]
    val rolesIdsOpt = Optional.ofNullable(userDto.getRolesIds)
    if (!rolesIdsOpt.isEmpty) rolesIdsOpt.get.forEach((roleId: Integer) => {
      def foo(roleId: Integer) = {
        val role = roleRepository.findById(roleId).get
        roles.add(role)
      }

      foo(roleId)
    })
    returnValue.setRoles(roles)
    returnValue
  }

  def roleEntityToDto(roleEntity: RoleEntity): RoleDto = {
    val returnValue = mapper.map(roleEntity, classOf[RoleDto])
    val users = roleEntity.getUsers
    val userIds = new util.ArrayList[Integer]
    users.forEach((user: UserEntity) => {
      def foo(user: UserEntity) = userIds.add(user.getId)

      foo(user)
    })
    returnValue.setUsersIds(userIds)
    returnValue
  }

  def roleDtoToEntity(roleDto: RoleDto): RoleEntity = {
    val returnValue = mapper.map(roleDto, classOf[RoleEntity])
    val usersIds = roleDto.getUsersIds
    val users = new util.ArrayList[UserEntity]
    usersIds.forEach((userId: Integer) => {
      def foo(userId: Integer) = {
        val userEntity = userRepository.findById(userId).get
        users.add(userEntity)
      }

      foo(userId)
    })
    returnValue.setUsers(users)
    returnValue
  }

  def cartItemDtoToOrderItemDto(cartItem: CartItemDto): OrderItemDto = {
    val returnValue = mapper.map(cartItem, classOf[OrderItemDto])
    returnValue
  }

  def cartItemEntityToOrderItemEntity(cartItem: CartItemEntity): OrderItemEntity = {
    val returnValue = mapper.map(cartItem, classOf[OrderItemEntity])
    returnValue
  }

  def cartItemToOrderItemEntity(cartItemEntity: CartItemEntity): OrderItemEntity = {
    val returnValue = mapper.map(cartItemEntity, classOf[OrderItemEntity])
    val productOpt = Optional.ofNullable(cartItemEntity.getProduct)
    if (productOpt.isPresent) {
      val product = productOpt.get
      returnValue.setProductName(product.getProductName)
      returnValue.setProductPrice(product.getProductPrice)
    }
    returnValue
  }

  def addressEntityToDto(addressEntity: AddressEntity): AddressDto = {
    val returnValue = mapper.map(addressEntity, classOf[AddressDto])
    val customerOpt = Optional.ofNullable(addressEntity.getCustomer)
    if (customerOpt.isPresent) returnValue.setCustomerId(customerOpt.get.getCustomerId)
    returnValue
  }

  def addressDtoToEntity(address: AddressDto): AddressEntity = {
    val returnValue = mapper.map(address, classOf[AddressEntity])
    val customerIdOpt = Optional.ofNullable(address.getCustomerId)
    if (customerIdOpt.isPresent) {
      val customerId = customerIdOpt.get
      val customerEntity = customerRepository.findById(customerId).get
      returnValue.setCustomer(customerEntity)
    }
    returnValue
  }

  def orderAddressEntityToDto(address: OrderAddressEntity): OrderAddressDto = {
    val returnValue = mapper.map(address, classOf[OrderAddressDto])
    val orderOpt = Optional.ofNullable(address.getOrder)
    if (orderOpt.isPresent) returnValue.setOrderId(orderOpt.get.getOrderId)
    returnValue
  }

  def orderAddressDtoToEntity(address: OrderAddressDto): OrderAddressEntity = {
    val returnValue = mapper.map(address, classOf[OrderAddressEntity])
    val orderIdOpt = Optional.ofNullable(address.getOrderId)
    if (orderIdOpt.isPresent) {
      val orderId = orderIdOpt.get
      val orderEntity = orderRepository.findById(orderId).get
      returnValue.setOrder(orderEntity)
    }
    returnValue
  }

  def addressToOrderAddress(address: AddressEntity): OrderAddressEntity = {
    val returnValue = mapper.map(address, classOf[OrderAddressEntity])
    returnValue
  }
}

