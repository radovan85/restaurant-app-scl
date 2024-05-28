package com.radovan.spring.controller

import com.radovan.spring.dto.ProductDto
import com.radovan.spring.services.{AddressService, CartItemService, CustomerService, OrderAddressService, OrderItemService, OrderService, ProductService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

@Controller
@RequestMapping(value = Array("/admin"))
class AdminController {

  private var productService:ProductService = _
  private var customerService:CustomerService = _
  private var userService:UserService = _
  private var addressService:AddressService = _
  private var orderService:OrderService = _
  private var orderItemService:OrderItemService = _
  private var orderAddressService:OrderAddressService = _
  private var cartItemService:CartItemService = _

  @Autowired
  private def injectAll(productService: ProductService,customerService: CustomerService,userService: UserService,
                        addressService: AddressService,orderService: OrderService,orderItemService: OrderItemService,
                        orderAddressService: OrderAddressService,cartItemService: CartItemService):Unit = {

    this.productService = productService
    this.customerService = customerService
    this.userService = userService
    this.addressService = addressService
    this.orderService = orderService
    this.orderItemService = orderItemService
    this.orderAddressService = orderAddressService
    this.cartItemService = cartItemService
  }

  @GetMapping(value = Array("/"))
  def adminHome: String = "fragments/admin :: fragmentContent"

  @GetMapping(value = Array("/createProduct"))
  def renderProductForm(map: ModelMap): String = {
    val product: ProductDto = new ProductDto
    map.put("product", product)
    map.put("allCategories", product.getCategoryList)
    "fragments/productForm :: fragmentContent"
  }

  @PostMapping(value = Array("/createProduct"))
  def createProduct(@ModelAttribute("product") product: ProductDto): String = {
    productService.addProduct(product)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/allProducts"))
  def allProductsList(map: ModelMap): String = {
    map.put("allProducts", productService.listAll())
    map.put("recordsPerPage", 5.asInstanceOf[Integer])
    "fragments/adminProductList :: fragmentContent"
  }

  @GetMapping(value = Array("/updateProduct/{productId}"))
  def renderUpdateForm(@PathVariable("productId") productId: Integer, map: ModelMap): String = {
    val product: ProductDto = new ProductDto
    val currentProduct: ProductDto = productService.getProductById(productId)
    map.put("product", product)
    map.put("currentProduct", currentProduct)
    map.put("allCategories", product.getCategoryList)
    "fragments/updateProduct :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteProduct/{productId}"))
  def deleteProduct(@PathVariable("productId") productId: Integer): String = {
    productService.deleteProduct(productId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/productDetails/{productId}"))
  def getProductDetails(@PathVariable("productId") productId: Integer, map: ModelMap): String = {
    map.put("currentProduct", productService.getProductById(productId))
    "fragments/productDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/allCustomers"))
  def listAllCustomers(map: ModelMap): String = {
    map.put("allCustomers", customerService.listAll())
    map.put("allUsers", userService.listAll())
    map.put("recordsPerPage", 7.asInstanceOf[Integer])
    "fragments/customerList :: fragmentContent"
  }

  @GetMapping(value = Array("/customerDetails/{customerId}"))
  def getCustomerDetails(@PathVariable("customerId") customerId: Integer, map: ModelMap): String = {
    val customer = customerService.getCustomerById(customerId)
    val address = addressService.getAddressById(customer.getAddressId)
    val user = userService.getUserById(customer.getUserId)
    val allOrders = orderService.listAllByCustomerId(customerId)
    map.put("customer", customer)
    map.put("address", address)
    map.put("user", user)
    map.put("allOrders", allOrders)
    "fragments/customerDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/allOrders"))
  def getAllOrders(map: ModelMap): String = {
    map.put("allOrders", orderService.listAll())
    map.put("allCustomers", customerService.listAll())
    map.put("allUsers", userService.listAll())
    map.put("recordsPerPage", 10.asInstanceOf[Integer])
    "fragments/orderList :: fragmentContent"
  }

  @GetMapping(value = Array("/allOrdersToday"))
  def getAllOrdersToday(map: ModelMap): String = {
    map.put("allOrders", orderService.getTodaysOrders())
    map.put("allCustomers", customerService.listAll())
    map.put("allUsers", userService.listAll())
    map.put("recordsPerPage", 10.asInstanceOf[Integer])
    "fragments/ordersTodayList :: fragmentContent"
  }

  @GetMapping(value = Array("/getOrder/{orderId}"))
  def orderDetails(@PathVariable("orderId") orderId: Integer, map: ModelMap): String = {
    val order = orderService.getOrderById(orderId)
    val customer = customerService.getCustomerByCartId(order.getCartId)
    val address = orderAddressService.getAddressById(order.getAddressId)
    val orderedItems = orderItemService.listAllByOrderId(orderId)
    map.put("order", order)
    map.put("address", address)
    map.put("orderedItems", orderedItems)
    map.put("customer", customer)
    "fragments/orderDetails :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteOrder/{orderId}"))
  def deleteOrder(@PathVariable("orderId") orderId: Integer): String = {
    orderService.deleteOrder(orderId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/deleteCustomer/{customerId}"))
  def removeCustomer(@PathVariable("customerId") customerId:Integer):String = {
    customerService.deleteCustomer(customerId)
    "fragments/homePage :: fragmentContent"
  }
}

