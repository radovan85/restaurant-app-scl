package com.radovan.spring.controller

import com.radovan.spring.dto.{AddressDto, CartDto, CustomerDto, OrderAddressDto, OrderDto, OrderItemDto, ProductDto, UserDto}
import com.radovan.spring.service.{AddressService, CartItemService, CartService, CustomerService, OrderAddressService, OrderItemService, OrderService, ProductService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

import java.util
import scala.collection.JavaConverters._

@Controller
@RequestMapping(value = Array("/admin"))
class AdminController {

  @Autowired
  private val productService: ProductService = null

  @Autowired
  private val customerService: CustomerService = null

  @Autowired
  private val userService: UserService = null

  @Autowired
  private val addressService: AddressService = null

  @Autowired
  private val orderService: OrderService = null

  @Autowired
  private val orderItemService: OrderItemService = null

  @Autowired
  private val orderAddressService: OrderAddressService = null

  @Autowired
  private val cartItemService: CartItemService = null

  @Autowired
  private val cartService: CartService = null

  @GetMapping(value = Array("/"))
  def adminHome: String = "fragments/admin :: ajaxLoadedContent"

  @GetMapping(value = Array("/createProduct"))
  def renderProductForm(map: ModelMap): String = {
    val product: ProductDto = new ProductDto
    map.put("product", product)
    map.put("allCategories", product.getCategoryList)
    "fragments/productForm :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/createProduct"))
  def createProduct(@ModelAttribute("product") product: ProductDto): String = {
    productService.addProduct(product)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allProducts"))
  def allProductsList(map: ModelMap): String = {
    val allProducts: util.List[ProductDto] = productService.listAll
    map.put("allProducts", allProducts)
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/adminProductList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/updateProduct/{productId}"))
  def renderUpdateForm(@PathVariable("productId") productId: Integer, map: ModelMap): String = {
    val product: ProductDto = new ProductDto
    val currentProduct: ProductDto = productService.getProduct(productId)
    map.put("product", product)
    map.put("currentProduct", currentProduct)
    map.put("allCategories", product.getCategoryList)
    "fragments/updateProduct :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteProduct/{productId}"))
  def deleteProduct(@PathVariable("productId") productId: Integer): String = {
    cartItemService.eraseAllByProductId(productId)
    productService.deleteProduct(productId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/productDetails/{productId}"))
  def getProductDetails(@PathVariable("productId") productId: Integer, map: ModelMap): String = {
    val currentProduct: ProductDto = productService.getProduct(productId)
    map.put("currentProduct", currentProduct)
    "fragments/productDetails :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allCustomers"))
  def listAllCustomers(map: ModelMap): String = {
    val allCustomers: util.List[CustomerDto] = customerService.listAll
    val allUsers: util.List[UserDto] = userService.listAllUsers
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 7.asInstanceOf[Integer])
    "fragments/customerList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/customerDetails/{customerId}"))
  def getCustomerDetails(@PathVariable("customerId") customerId: Integer, map: ModelMap): String = {
    val customer: CustomerDto = customerService.getCustomer(customerId)
    val address: AddressDto = addressService.getAddressById(customer.getAddressId)
    val user: UserDto = userService.getUserById(customer.getUserId)
    val allOrders: util.List[OrderDto] = orderService.listAllByCustomerId(customerId)
    map.put("customer", customer)
    map.put("address", address)
    map.put("user", user)
    map.put("allOrders", allOrders)
    "fragments/customerDetails :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allOrders"))
  def getAllOrders(map: ModelMap): String = {
    val allOrders: util.List[OrderDto] = orderService.listAll
    val allCustomers: util.List[CustomerDto] = customerService.listAll
    val allUsers: util.List[UserDto] = userService.listAllUsers
    map.put("allOrders", allOrders)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 10.asInstanceOf[Integer])
    "fragments/orderList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allOrdersToday"))
  def getAllOrdersToday(map: ModelMap): String = {
    val allOrders: util.List[OrderDto] = orderService.getTodaysOrders
    val allCustomers: util.List[CustomerDto] = customerService.listAll
    val allUsers: util.List[UserDto] = userService.listAllUsers
    map.put("allOrders", allOrders)
    map.put("allCustomers", allCustomers)
    map.put("allUsers", allUsers)
    map.put("recordsPerPage", 10.asInstanceOf[Integer])
    "fragments/ordersTodayList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/getOrder/{orderId}"))
  def orderDetails(@PathVariable("orderId") orderId: Integer, map: ModelMap): String = {
    val order: OrderDto = orderService.getOrder(orderId)
    val customer: CustomerDto = customerService.getCustomer(order.getCustomerId)
    val address: OrderAddressDto = orderAddressService.getAddressById(order.getAddressId)
    val orderPrice: Float = orderService.calculateOrderPrice(orderId)
    val orderedItems: util.List[OrderItemDto] = orderItemService.listAllByOrderId(orderId)
    map.put("order", order)
    map.put("address", address)
    map.put("orderPrice", orderPrice.asInstanceOf[java.lang.Float])
    map.put("orderedItems", orderedItems)
    map.put("customer", customer)
    "fragments/orderDetails :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteOrder/{orderId}"))
  def deleteOrder(@PathVariable("orderId") orderId: Integer): String = {
    val order: OrderDto = orderService.getOrder(orderId)
    val addressId: Integer = order.getAddressId
    orderItemService.eraseAllByOrderId(orderId)
    orderService.deleteOrder(orderId)
    orderAddressService.deleteAddress(addressId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/invalidPath")) def invalidImagePath: String = "fragments/invalidImagePath :: ajaxLoadedContent"

  @GetMapping(value = Array("/deleteCustomer/{customerId}"))
  def removeCustomer(@PathVariable("customerId") customerId: Integer): String = {
    val customer: CustomerDto = customerService.getCustomer(customerId)
    val cart: CartDto = cartService.getCartByCartId(customer.getCartId)
    val address: AddressDto = addressService.getAddressById(customer.getAddressId)
    val user: UserDto = userService.getUserById(customer.getUserId)
    val allOrders: util.List[OrderDto] = orderService.listAllByCustomerId(customerId)
    for (order <- allOrders.asScala) {
      orderItemService.eraseAllByOrderId(order.getOrderId)
      orderService.deleteOrder(order.getOrderId)
      orderAddressService.deleteAddress(order.getAddressId)
    }
    cartItemService.eraseAllCartItems(cart.getCartId)
    customerService.resetCustomer(customerId)
    addressService.deleteAddress(address.getAddressId)
    cartService.deleteCart(cart.getCartId)
    customerService.deleteCustomer(customerId)
    userService.deleteUser(user.getId)
    "fragments/homePage :: ajaxLoadedContent"
  }
}

