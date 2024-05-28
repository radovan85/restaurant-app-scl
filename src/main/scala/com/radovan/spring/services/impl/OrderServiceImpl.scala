package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderDto
import com.radovan.spring.entity.OrderItemEntity
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repository.{OrderAddressRepository, OrderItemRepository, OrderRepository}
import com.radovan.spring.services.{AddressService, CartItemService, CartService, CustomerService, OrderService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.sql.Timestamp
import java.time.{Instant, ZoneId}
import scala.collection.mutable.ArrayBuffer
import scala.collection.JavaConverters._

@Service
class OrderServiceImpl extends OrderService{

  private var orderRepository:OrderRepository = _
  private var orderAddressRepository:OrderAddressRepository = _
  private var orderItemRepository:OrderItemRepository = _
  private var customerService:CustomerService = _
  private var cartService:CartService = _
  private var addressService:AddressService = _
  private var cartItemService:CartItemService = _
  private var tempConverter:TempConverter = _
  private val zoneId = ZoneId.of("Europe/Belgrade")

  @Autowired
  private def injectAll(orderRepository: OrderRepository,orderAddressRepository: OrderAddressRepository,orderItemRepository: OrderItemRepository,
                        customerService: CustomerService,cartService: CartService,addressService: AddressService,
                        tempConverter: TempConverter,cartItemService: CartItemService):Unit = {

    this.orderRepository = orderRepository
    this.orderAddressRepository = orderAddressRepository
    this.orderItemRepository = orderItemRepository
    this.customerService = customerService
    this.addressService = addressService
    this.cartService = cartService
    this.tempConverter = tempConverter
    this.cartItemService = cartItemService
  }

  @Transactional
  override def addOrder(): OrderDto = {
    var returnValue = new OrderDto
    val customer = customerService.getCurrentCustomer()
    val cart = cartService.validateCart(customer.getCartId)
    val address = addressService.getAddressById(customer.getAddressId)
    val orderAddress = tempConverter.addressToOrderAddress(address)
    val storedAddress = orderAddressRepository.save(tempConverter.orderAddressDtoToEntity(orderAddress))


    returnValue.setCartId(cart.getId)
    returnValue.setOrderPrice(cart.getCartPrice)
    returnValue.setAddressId(storedAddress.getId)
    val orderEntity = tempConverter.orderDtoToEntity(returnValue)
    val currentTime = Instant.now().atZone(zoneId)
    orderEntity.setCreatedAt(Timestamp.valueOf(currentTime.toLocalDateTime))
    var storedOrder = orderRepository.save(orderEntity)

    val allCartItems = cartItemService.listAllByCartId(cart.getId)
    val allOrderedItems = new ArrayBuffer[OrderItemEntity]()

    allCartItems.foreach(cartItem => {
      val orderItem = tempConverter.cartItemToOrderItem(cartItem)
      orderItem.setOrderId(storedOrder.getId)
      val storedItem = orderItemRepository.save(tempConverter.orderItemDtoToEntity(orderItem))
      allOrderedItems += storedItem
    })

    storedOrder.getOrderedItems.clear()
    storedOrder.getOrderedItems.addAll(allOrderedItems.asJava)
    storedOrder = orderRepository.saveAndFlush(storedOrder)
    returnValue = tempConverter.orderEntityToDto(storedOrder)
    cartItemService.eraseAllCartItems(cart.getId)
    cartService.refreshCartState(cart.getId)
    returnValue

  }

  @Transactional(readOnly = true)
  override def getTodaysOrders(): Array[OrderDto] = {
    val allOrders = orderRepository.findAll().asScala
    val today = Instant.now().atZone(zoneId).toLocalDate
    val ordersToday = allOrders.collect {
      case order if order.getCreatedAt.toInstant.atZone(zoneId).toLocalDate.isEqual(today) =>
        tempConverter.orderEntityToDto(order)
    }

    ordersToday.toArray
  }

  @Transactional(readOnly = true)
  override def listAllByCustomerId(customerId: Integer): Array[OrderDto] = {
    val allOrders = orderRepository.findAll().asScala
    val customer = customerService.getCustomerById(customerId)
    allOrders.collect {
      case order if order.getCart.getId == customer.getCartId => tempConverter.orderEntityToDto(order)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def calculateOrderPrice(orderId: Integer): Float = {
    orderItemRepository.calculateGrandTotal(orderId).getOrElse(0f)
  }

  @Transactional(readOnly = true)
  override def listAll(): Array[OrderDto] = {
    val allOrders = orderRepository.findAll().asScala
    allOrders.collect{
      case order => tempConverter.orderEntityToDto(order)
    }.toArray
  }

  @Transactional(readOnly = true)
  override def getOrderById(orderId: Integer): OrderDto = {
    val orderEntity = orderRepository.findById(orderId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The order has not been found!")))
    tempConverter.orderEntityToDto(orderEntity)
  }

  @Transactional
  override def deleteOrder(orderId: Integer): Unit = {
    getOrderById(orderId)
    orderRepository.deleteById(orderId)
    orderRepository.flush()
  }
}
