package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderItemDto
import com.radovan.spring.entity.OrderItemEntity
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repository.OrderItemRepository
import com.radovan.spring.services.OrderItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class OrderItemServiceImpl extends OrderItemService{

  private var itemRepository:OrderItemRepository = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def injectAll(itemRepository: OrderItemRepository,tempConverter: TempConverter):Unit = {
    this.itemRepository = itemRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def getItemById(itemId: Integer): OrderItemDto = {
    val itemEntity:OrderItemEntity = itemRepository.findById(itemId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The order item has not been found!")))
    tempConverter.orderItemEntityToDto(itemEntity)
  }

  @Transactional(readOnly = true)
  override def listAllByOrderId(orderId: Integer): Array[OrderItemDto] = {
    val allItems = itemRepository.findAll().asScala
    allItems.collect {
      case item if item.getOrder.getId == orderId => tempConverter.orderItemEntityToDto(item)
    }.toArray
  }
}
