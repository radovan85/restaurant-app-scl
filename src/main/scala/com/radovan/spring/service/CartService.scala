package com.radovan.spring.service

import com.radovan.spring.dto.CartDto

trait CartService {

  def getCartByCartId(cartId: Integer): CartDto

  def calculateCartPrice(cartId: Integer): Float

  def refreshCartState(cartId: Integer): Unit

  def validateCart(cartId: Integer): CartDto

  def deleteCart(cartId: Integer): Unit
}
