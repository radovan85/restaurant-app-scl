package com.radovan.spring.services

import com.radovan.spring.dto.CartItemDto

trait CartItemService {

  def addCartItem(cartItem:CartItemDto):CartItemDto
  def removeCartItem(itemId:Integer):Unit
  def eraseAllCartItems(cartId:Integer):Unit
  def listAllByCartId(cartId:Integer):Array[CartItemDto]
  def getItemById(itemId:Integer):CartItemDto
  def getCartItemByCartIdAndProductIdAndHotnessLevel(cartId:Integer,productId:Integer,hotnessLevel:String):CartItemDto
  def eraseAllByProductId(productId:Integer):Unit
}
