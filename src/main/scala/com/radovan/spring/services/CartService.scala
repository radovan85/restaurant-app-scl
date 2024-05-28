package com.radovan.spring.services

import com.radovan.spring.dto.CartDto

trait CartService {

  def getCartById(cartId: Integer): CartDto

  def calculateCartPrice(cartId: Integer): Float

  def refreshCartState(cartId: Integer): Unit

  def refreshAllCarts(): Unit

  def validateCart(cartId: Integer): CartDto
}

