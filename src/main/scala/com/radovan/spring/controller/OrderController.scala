package com.radovan.spring.controller

import com.radovan.spring.dto.OrderDto
import com.radovan.spring.services.{AddressService, CartItemService, CartService, CustomerService, OrderService, ProductService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping(value = Array("/orders"))
class OrderController {

   private var cartService:CartService = _
   private var customerService:CustomerService = _
   private var cartItemService:CartItemService = _
   private var addressService:AddressService = _
   private var productService:ProductService = _
   private var orderService:OrderService = _

  @Autowired
  private def injectAll(cartService: CartService,customerService: CustomerService,cartItemService: CartItemService,
                        addressService: AddressService,productService: ProductService,orderService: OrderService):Unit = {
    this.cartService = cartService
    this.customerService = customerService
    this.cartItemService = cartItemService
    this.addressService = addressService
    this.productService = productService
    this.orderService = orderService
  }

  @GetMapping(value = Array("/confirmOrder/{cartId}"))
  def confirmOrder(@PathVariable("cartId") cartId: Integer, map: ModelMap): String = {
    val order = new OrderDto
    cartService.validateCart(cartId)
    val customer = customerService.getCustomerByCartId(cartId)
    val allCartItems = cartItemService.listAllByCartId(cartId)
    val address = addressService.getAddressById(customer.getAddressId)
    val cart = cartService.getCartById(customer.getCartId)
    val allProducts = productService.listAll()
    map.put("order", order)
    map.put("customer", customer)
    map.put("allCartItems", allCartItems)
    map.put("address", address)
    map.put("cart", cart)
    map.put("allProducts", allProducts)
    "fragments/orderConfirmation :: fragmentContent"
  }

  @PostMapping(value = Array("/processOrder"))
  def processOrder: String = {
    orderService.addOrder()
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/orderCompleted"))
  def orderCompletedReport = "fragments/orderCompleted :: fragmentContent"
}
