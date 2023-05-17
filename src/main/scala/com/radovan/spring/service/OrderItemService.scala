package com.radovan.spring.service

import java.util
import com.radovan.spring.dto.OrderItemDto

trait OrderItemService {

  def getItem(itemId: Integer): OrderItemDto

  def listAllByOrderId(orderId: Integer): util.List[OrderItemDto]

  def eraseAllByOrderId(orderId: Integer): Unit
}
