package com.radovan.spring.service.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderDto
import com.radovan.spring.entity.{AddressEntity, CartEntity, CartItemEntity, CustomerEntity, OrderAddressEntity, OrderEntity, OrderItemEntity, UserEntity}
import com.radovan.spring.repository.{CartItemRepository, CustomerRepository, OrderAddressRepository, OrderItemRepository, OrderRepository}
import com.radovan.spring.service.{CartService, OrderService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.sql.Timestamp
import java.text.DecimalFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util
import java.util.Optional
import scala.collection.JavaConverters._

@Service
@Transactional
class OrderServiceImpl extends OrderService {

  @Autowired
  private val orderRepository: OrderRepository = null

  @Autowired
  private val tempConverter: TempConverter = null

  @Autowired
  private val orderItemRepository: OrderItemRepository = null

  @Autowired
  private val customerRepository: CustomerRepository = null

  @Autowired
  private val cartItemRepository: CartItemRepository = null

  @Autowired
  private val cartService: CartService = null

  @Autowired
  private val orderAddressRepository: OrderAddressRepository = null

  private val decfor: DecimalFormat = new DecimalFormat("0.00")

  override def addOrder(): OrderDto = {
    var returnValue: OrderDto = null
    val authUser: UserEntity = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    val customerOptional: Optional[CustomerEntity] = Optional.ofNullable(customerRepository.findByUserId(authUser.getId))
    var customerEntity: CustomerEntity = null
    val orderEntity: OrderEntity = new OrderEntity
    var orderedItems: util.List[OrderItemEntity] = new util.ArrayList[OrderItemEntity]
    if (customerOptional.isPresent) {
      customerEntity = customerOptional.get
      val cartOptional: Optional[CartEntity] = Optional.ofNullable(customerEntity.getCart)
      if (cartOptional.isPresent) {
        val cartEntity: CartEntity = cartOptional.get
        val allCartItemsOpt: Optional[util.List[CartItemEntity]] = Optional.ofNullable(cartEntity.getCartItems)
        if (!allCartItemsOpt.isEmpty) {
          for (cartItem <- allCartItemsOpt.get.asScala) {
            val orderItem: OrderItemEntity = tempConverter.cartItemToOrderItemEntity(cartItem)
            orderedItems.add(orderItem)
          }
          cartItemRepository.removeAllByCartId(cartEntity.getCartId)
          cartService.refreshCartState(cartEntity.getCartId)
          val address: AddressEntity = customerEntity.getAddress
          val orderAddress: OrderAddressEntity = tempConverter.addressToOrderAddress(address)
          val storedOrderAddress: OrderAddressEntity = orderAddressRepository.save(orderAddress)
          orderEntity.setCustomer(customerEntity)
          orderEntity.setPrice(cartEntity.getCartPrice)
          orderEntity.setAddress(storedOrderAddress)
          val createdAt: Timestamp = Timestamp.valueOf(LocalDateTime.now)
          orderEntity.setCreatedAt(createdAt)
          var storedOrder: OrderEntity = orderRepository.save(orderEntity)
          for (orderItem <- orderedItems.asScala) {
            orderItem.setOrder(storedOrder)
            orderItemRepository.save(orderItem)
          }
          orderedItems = orderItemRepository.findAllByOrderId(storedOrder.getOrderId)
          val orderPrice: Optional[Float] = Optional.ofNullable(orderItemRepository.calculateGrandTotal(storedOrder.getOrderId))
          if (orderPrice.isPresent) storedOrder.setPrice(orderPrice.get)
          storedOrderAddress.setOrder(storedOrder)
          orderAddressRepository.saveAndFlush(storedOrderAddress)
          storedOrder.setOrderItems(orderedItems)
          storedOrder = orderRepository.saveAndFlush(storedOrder)
          returnValue = tempConverter.orderEntityToDto(storedOrder)
        }
      }
    }
    returnValue
  }

  override def getTodaysOrders: util.List[OrderDto] = {
    val returnValue: util.List[OrderDto] = new util.ArrayList[OrderDto]
    val currentDate: LocalDateTime = LocalDateTime.now
    var formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    val currentDateStr: String = currentDate.format(formatter)
    val timestamp1Str: String = currentDateStr + " 00:00:00"
    val timestamp2Str: String = currentDateStr + " 23:59:59"
    formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
    val timestamp1: Timestamp = Timestamp.valueOf(LocalDateTime.parse(timestamp1Str, formatter))
    val timestamp2: Timestamp = Timestamp.valueOf(LocalDateTime.parse(timestamp2Str, formatter))
    val allOrdersOpt: Optional[util.List[OrderEntity]] = Optional.ofNullable(orderRepository.findAllTodaysOrders(timestamp1, timestamp2))
    if (!allOrdersOpt.isEmpty) allOrdersOpt.get.forEach((order: OrderEntity) => {
      def foo(order: OrderEntity) = {
        val orderDto: OrderDto = tempConverter.orderEntityToDto(order)
        returnValue.add(orderDto)
      }

      foo(order)
    })
    returnValue
  }

  override def listAllByCustomerId(customerId: Integer): util.List[OrderDto] = {
    val returnValue: util.List[OrderDto] = new util.ArrayList[OrderDto]
    val allOrdersOpt: Optional[util.List[OrderEntity]] = Optional.ofNullable(orderRepository.findAllByCustomerId(customerId))
    if (!allOrdersOpt.isEmpty) allOrdersOpt.get.forEach((order: OrderEntity) => {
      def foo(order: OrderEntity) = {
        val orderDto: OrderDto = tempConverter.orderEntityToDto(order)
        returnValue.add(orderDto)
      }

      foo(order)
    })
    returnValue
  }

  override def calculateOrderPrice(orderId: Integer): Float = {
    val orderTotalOpt: Optional[Float] = Optional.ofNullable(orderItemRepository.calculateGrandTotal(orderId))
    var returnValue: Float = 0f
    if (orderTotalOpt.isPresent) {
      returnValue = orderTotalOpt.get
      returnValue = decfor.format(returnValue).toFloat
    }
    returnValue
  }

  override def listAll: util.List[OrderDto] = {
    val allOrders: util.List[OrderEntity] = orderRepository.findAll
    val returnValue: util.List[OrderDto] = new util.ArrayList[OrderDto]
    allOrders.forEach((order: OrderEntity) => {
      def foo(order: OrderEntity) = {
        val orderDto: OrderDto = tempConverter.orderEntityToDto(order)
        returnValue.add(orderDto)
      }

      foo(order)
    })
    returnValue
  }

  override def getOrder(orderId: Integer): OrderDto = {
    var returnValue: OrderDto = null
    val orderOpt: Optional[OrderEntity] = orderRepository.findById(orderId)
    if (orderOpt.isPresent) returnValue = tempConverter.orderEntityToDto(orderOpt.get)
    returnValue
  }

  override def deleteOrder(orderId: Integer): Unit = {
    orderRepository.eraseById(orderId)
    orderRepository.flush()
  }
}
