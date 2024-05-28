package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartDto
import com.radovan.spring.exceptions.{InstanceUndefinedException, InvalidCartException}
import com.radovan.spring.repository.{CartItemRepository, CartRepository}
import com.radovan.spring.services.CartService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class CartServiceImpl extends CartService {

  private var cartRepository:CartRepository = _
  private var tempConverter:TempConverter = _
  private var cartItemRepository:CartItemRepository = _

  @Autowired
  private def injectAll(cartRepository: CartRepository,tempConverter: TempConverter,cartItemRepository: CartItemRepository):Unit = {
    this.cartRepository = cartRepository
    this.tempConverter = tempConverter
    this.cartItemRepository = cartItemRepository
  }

  @Transactional(readOnly = true)
  override def getCartById(cartId: Integer): CartDto = {
    val cartEntity = cartRepository.findById(cartId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The cart has not been found!")))
    tempConverter.cartEntityToDto(cartEntity)
  }

  @Transactional(readOnly = true)
  override def calculateCartPrice(cartId: Integer): Float = {
    cartItemRepository.calculateGrandTotal(cartId).getOrElse(0f)
  }

  @Transactional
  override def refreshCartState(cartId: Integer): Unit = {
    val cart = getCartById(cartId)
    cart.setCartPrice(calculateCartPrice(cart.getId))
    cartRepository.saveAndFlush(tempConverter.cartDtoToEntity(cart))
  }

  @Transactional
  override def refreshAllCarts(): Unit = {
    val allCarts = cartRepository.findAll().asScala
    allCarts.foreach(cartEntity => refreshCartState(cartEntity.getId))
  }

  @Transactional(readOnly = true)
  override def validateCart(cartId: Integer): CartDto = {
    val cart = getCartById(cartId)
    if(cart.getCartItemsIds.length == 0){
      throw new InvalidCartException(new Error("The cart is empty!"))
    }

    cart
  }
}
