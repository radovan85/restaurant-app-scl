package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.entity.CartItemEntity
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repository.CartItemRepository
import com.radovan.spring.services.{CartItemService, CartService, CustomerService, ProductService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class CartItemServiceImpl extends CartItemService{

  private var cartItemRepository:CartItemRepository = _
  private var customerService:CustomerService = _
  private var cartService:CartService = _
  private var productService:ProductService = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def injectAll(cartItemRepository: CartItemRepository,customerService: CustomerService,cartService: CartService,
                        productService: ProductService,tempConverter: TempConverter):Unit = {
    this.cartItemRepository = cartItemRepository
    this.customerService = customerService
    this.cartService = cartService
    this.productService = productService
    this.tempConverter = tempConverter
  }


  @Transactional
  override def addCartItem(cartItem: CartItemDto): CartItemDto = {
    var returnValue:CartItemDto = null
    val productId = cartItem.getProductId
    val hotnessLevel = cartItem.getHotnessLevel
    val customer = customerService.getCurrentCustomer()
    val cart = cartService.getCartById(customer.getCartId)
    val product = productService.getProductById(productId)

    val existingItem:CartItemEntity = cartItemRepository.findByCartIdAndProductIdAndHotnessLevel(cart.getId, product.getId, hotnessLevel).getOrElse(null)
    if(existingItem!=null){
      cartItem.setId(existingItem.getId)
      cartItem.setCartId(cart.getId)
      cartItem.setQuantity(existingItem.getQuantity + cartItem.getQuantity)
      if(cartItem.getQuantity > 50){
        cartItem.setQuantity(50)
      }
      cartItem.setPrice(product.getProductPrice * cartItem.getQuantity)
      val cartItemEntity = tempConverter.cartItemDtoToEntity(cartItem)
      val updatedItem = cartItemRepository.saveAndFlush(cartItemEntity)
      returnValue = tempConverter.cartItemEntityToDto(updatedItem)
    }else {
      if(cartItem.getQuantity > 50){
        cartItem.setQuantity(50)
      }
      cartItem.setCartId(cart.getId)
      cartItem.setPrice(product.getProductPrice * cartItem.getQuantity)
      val storedItem = cartItemRepository.save(tempConverter.cartItemDtoToEntity(cartItem))
      returnValue = tempConverter.cartItemEntityToDto(storedItem)

    }

    cartService.refreshCartState(cart.getId)
    returnValue
  }

  @Transactional
  override def removeCartItem(itemId: Integer): Unit = {
    val cartItem = getItemById(itemId)
    val cartId = cartItem.getCartId
    cartItemRepository.removeCartItem(itemId)
    cartItemRepository.flush()
    cartService.refreshCartState(cartId)
  }

  @Transactional
  override def eraseAllCartItems(cartId: Integer): Unit = {
    val allCartItems = listAllByCartId(cartId)
    allCartItems.foreach(cartItem => removeCartItem(cartItem.getId))
  }

  @Transactional(readOnly = true)
  override def listAllByCartId(cartId: Integer): Array[CartItemDto] = {
    val allCartItems = cartItemRepository.findAllByCartId(cartId).asScala
    allCartItems.map(cartItem => tempConverter.cartItemEntityToDto(cartItem)).toArray
  }

  @Transactional(readOnly = true)
  override def getItemById(itemId: Integer): CartItemDto = {
    val itemEntity = cartItemRepository.findById(itemId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The item has not been found!")))
    tempConverter.cartItemEntityToDto(itemEntity)
  }

  @Transactional(readOnly = true)
  override def getCartItemByCartIdAndProductIdAndHotnessLevel(cartId: Integer, productId: Integer, hotnessLevel: String): CartItemDto = {
    var returnValue:CartItemDto = null
    val itemEntity = cartItemRepository.findByCartIdAndProductIdAndHotnessLevel(cartId, productId, hotnessLevel).getOrElse(null)
    if(itemEntity!=null){
      returnValue = tempConverter.cartItemEntityToDto(itemEntity)
    }
    returnValue
  }

  @Transactional
  override def eraseAllByProductId(productId: Integer): Unit = {
    val allCartItems = cartItemRepository.findAllByProductId(productId).asScala
    allCartItems.foreach(cartItem => removeCartItem(cartItem.getId))
  }
}
