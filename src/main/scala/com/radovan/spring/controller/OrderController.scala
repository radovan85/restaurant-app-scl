package com.radovan.spring.controller

import com.radovan.spring.dto.{AddressDto, CartDto, CartItemDto, CustomerDto, OrderDto, ProductDto}
import com.radovan.spring.service.{AddressService, CartItemService, CartService, CustomerService, OrderService, ProductService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, PostMapping, RequestMapping}

import java.util

@Controller
@RequestMapping(value = Array("/orders"))
class OrderController {

  @Autowired
  private val cartService: CartService = null

  @Autowired
  private val customerService: CustomerService = null

  @Autowired
  private val cartItemService: CartItemService = null

  @Autowired
  private val addressService: AddressService = null

  @Autowired
  private val productService: ProductService = null

  @Autowired
  private val orderService: OrderService = null

  @GetMapping(value = Array("/confirmOrder/{cartId}"))
  def confirmOrder(@PathVariable("cartId") cartId: Integer, map: ModelMap): String = {
    val order: OrderDto = new OrderDto
    cartService.validateCart(cartId)
    val customer: CustomerDto = customerService.getCustomerByCartId(cartId)
    val allCartItems: util.List[CartItemDto] = cartItemService.listAllByCartId(cartId)
    val address: AddressDto = addressService.getAddressById(customer.getAddressId)
    val cart: CartDto = cartService.getCartByCartId(customer.getCartId)
    val allProducts: util.List[ProductDto] = productService.listAll
    map.put("order", order)
    map.put("customer", customer)
    map.put("allCartItems", allCartItems)
    map.put("address", address)
    map.put("cart", cart)
    map.put("allProducts", allProducts)
    "fragments/orderConfirmation :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/processOrder"))
  def processOrder: String = {
    orderService.addOrder()
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/orderCompleted"))
  def orderCompletedReport: String = "fragments/orderCompleted :: ajaxLoadedContent"
}

