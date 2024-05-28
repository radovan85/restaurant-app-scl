package com.radovan.spring.controller

import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.services.{CartItemService, CartService, CustomerService, ProductService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

@Controller
@RequestMapping(value=Array("/cart"))
class CartController {

  private var cartService:CartService = _
  private var customerService:CustomerService = _
  private var productService:ProductService = _
  private var cartItemService:CartItemService = _

  @Autowired
  def injectAll(cartService: CartService,customerService: CustomerService,
                productService: ProductService,cartItemService: CartItemService):Unit = {
    this.cartService = cartService
    this.customerService = customerService
    this.productService = productService
    this.cartItemService = cartItemService
  }

  @GetMapping(value=Array("/addToCart/{productId}"))
  def renderItemForm(@PathVariable("productId") productId:Integer,map:ModelMap):String = {
    val cartItem = new CartItemDto
    val selectedProduct = productService.getProductById(productId)
    map.put("cartItem", cartItem)
    map.put("selectedProduct", selectedProduct)
    map.put("allHotnessLevels", cartItem.getHotnessLevelList)
    "fragments/cartItemForm :: fragmentContent"
  }

  @PostMapping(value=Array("/addToCart"))
  def addCartItem(@ModelAttribute("cartItem") cartItem:CartItemDto):String = {
    cartItemService.addCartItem(cartItem)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/itemAddCompleted"))
  def itemAdded = "fragments/itemAdded :: fragmentContent"

  @GetMapping(value=Array("/getCart"))
  def cartDetails(map:ModelMap):String = {
    val customer = customerService.getCurrentCustomer()
    val cart = cartService.getCartById(customer.getCartId)
    val allCartItems = cartItemService.listAllByCartId(cart.getId)
    val allProducts = productService.listAll()
    map.put("allCartItems", allCartItems)
    map.put("allProducts", allProducts)
    map.put("cart", cart)
    "fragments/cart :: fragmentContent"
  }

  @GetMapping(value=Array("/deleteItem/{itemId}"))
  def deleteCartItem(@PathVariable("itemId") itemId:Integer):String = {
    cartItemService.removeCartItem(itemId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value=Array("/deleteAllItems/{cartId}"))
  def deleteAllCartItems(@PathVariable("cartId") cartId:Integer):String = {
    cartItemService.eraseAllCartItems(cartId)
    "fragments/homePage :: fragmentContent"
  }

  @GetMapping(value = Array("/invalidCart"))
  def invalidCartEx(): String = "fragments/invalidCart :: fragmentContent"
}
