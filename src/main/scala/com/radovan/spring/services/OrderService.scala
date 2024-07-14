package com.radovan.spring.services

import com.radovan.spring.dto.OrderDto

trait OrderService {

  def addOrder():OrderDto
  def getTodaysOrders():Array[OrderDto]
  def listAllByCustomerId(customerId:Integer):Array[OrderDto]
  def calculateOrderPrice(orderId:Integer):Float
  def listAll():Array[OrderDto]
  def getOrderById(orderId:Integer):OrderDto
  def deleteOrder(orderId:Integer):Unit
}
