package com.radovan.spring.services

import com.radovan.spring.dto.OrderItemDto

trait OrderItemService {

  def getItemById(itemId:Integer):OrderItemDto
  def listAllByOrderId(orderId:Integer):Array[OrderItemDto]
}
