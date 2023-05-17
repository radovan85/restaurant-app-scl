package com.radovan.spring.controller

import com.radovan.spring.dto.{CartDto, CartItemDto, CustomerDto, ProductDto, UserDto}
import com.radovan.spring.service.{CartItemService, CartService, CustomerService, ProductService, UserService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, ModelAttribute, PathVariable, PostMapping, RequestMapping}

import java.util
import java.util.Optional

@Controller
@RequestMapping(value = Array("/cart"))
class CartController {

  @Autowired
  private val productService: ProductService = null

  @Autowired
  private val userService: UserService = null

  @Autowired
  private val customerService: CustomerService = null

  @Autowired
  private val cartService: CartService = null

  @Autowired
  private val cartItemService: CartItemService = null

  @GetMapping(value = Array("/addToCart/{productId}"))
  def renderItemForm(@PathVariable("productId") productId: Integer, map: ModelMap): String = {
    val cartItem: CartItemDto = new CartItemDto
    val selectedProduct: ProductDto = productService.getProduct(productId)
    map.put("cartItem", cartItem)
    map.put("selectedProduct", selectedProduct)
    map.put("allHotnessLevels", cartItem.getHotnessLevelList)
    "fragments/cartItemForm :: ajaxLoadedContent"
  }

  @PostMapping(value = Array("/addToCart"))
  def addCartItem(@ModelAttribute("cartItem") cartItem: CartItemDto): String = {
    val productId: Integer = cartItem.getProductId
    val hotnessLevel: String = cartItem.getHotnessLevel
    val authUser: UserDto = userService.getCurrentUser
    val customer: CustomerDto = customerService.getCustomerByUserId(authUser.getId)
    val cart: CartDto = cartService.getCartByCartId(customer.getCartId)
    val product: ProductDto = productService.getProduct(productId)
    val existingCartItem: Optional[CartItemDto] = Optional.ofNullable(cartItemService.getCartItemByCartIdAndProductIdAndHotnessLevel(cart.getCartId, productId, hotnessLevel))
    if (existingCartItem.isPresent) {
      cartItem.setCartItemId(existingCartItem.get.getCartItemId)
      cartItem.setCartId(cart.getCartId)
      cartItem.setQuantity(existingCartItem.get.getQuantity + cartItem.getQuantity)
      if (cartItem.getQuantity > 50) cartItem.setQuantity(50)
      cartItem.setPrice(product.getProductPrice * cartItem.getQuantity)
      cartItemService.addCartItem(cartItem)
      cartService.refreshCartState(cart.getCartId)
    }
    else {
      cartItem.setQuantity(cartItem.getQuantity)
      if (cartItem.getQuantity > 50) cartItem.setQuantity(50)
      cartItem.setCartId(cart.getCartId)
      cartItem.setPrice(product.getProductPrice * cartItem.getQuantity)
      cartItemService.addCartItem(cartItem)
      cartService.refreshCartState(cart.getCartId)
    }
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/itemAddCompleted"))
  def itemAdded: String = "fragments/itemAdded :: ajaxLoadedContent"

  @GetMapping(value = Array("/getCart"))
  def cartDetails(map: ModelMap): String = {
    val authUser: UserDto = userService.getCurrentUser
    val customer: CustomerDto = customerService.getCustomerByUserId(authUser.getId)
    val cart: CartDto = cartService.getCartByCartId(customer.getCartId)
    val allCartItems: util.List[CartItemDto] = cartItemService.listAllByCartId(customer.getCartId)
    val allProducts: util.List[ProductDto] = productService.listAll
    map.put("allCartItems", allCartItems)
    map.put("allProducts", allProducts)
    map.put("cart", cart)
    "fragments/cart :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteItem/{cartId}/{itemId}"))
  def deleteCartItem(@PathVariable("cartId") cartId: Integer, @PathVariable("itemId") itemId: Integer): String = {
    cartItemService.removeCartItem(cartId, itemId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/deleteAllItems/{cartId}"))
  def deleteAllCartItems(@PathVariable("cartId") cartId: Integer): String = {
    cartItemService.eraseAllCartItems(cartId)
    "fragments/homePage :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/invalidCart"))
  def invalidCartEx(): String = "fragments/invalidCart :: ajaxLoadedContent"
}

