package com.radovan.spring.service.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.OrderItemDto
import com.radovan.spring.entity.OrderItemEntity
import com.radovan.spring.repository.OrderItemRepository
import com.radovan.spring.service.OrderItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.util
import java.util.Optional

@Service
@Transactional class OrderItemServiceImpl extends OrderItemService {

  @Autowired
  private val itemRepository: OrderItemRepository = null

  @Autowired
  private val tempConverter: TempConverter = null

  override def getItem(itemId: Integer): OrderItemDto = {
    var returnValue: OrderItemDto = null
    val itemOpt: Optional[OrderItemEntity] = itemRepository.findById(itemId)
    if (itemOpt.isPresent) returnValue = tempConverter.orderItemEntityToDto(itemOpt.get)
    returnValue
  }

  override def listAllByOrderId(orderId: Integer): util.List[OrderItemDto] = {
    val allItemsOpt: Optional[util.List[OrderItemEntity]] = Optional.ofNullable(itemRepository.findAllByOrderId(orderId))
    val returnValue: util.List[OrderItemDto] = new util.ArrayList[OrderItemDto]
    if (!allItemsOpt.isEmpty) allItemsOpt.get.forEach((itemEntity: OrderItemEntity) => {
      def foo(itemEntity: OrderItemEntity) = {
        val itemDto: OrderItemDto = tempConverter.orderItemEntityToDto(itemEntity)
        returnValue.add(itemDto)
      }

      foo(itemEntity)
    })
    returnValue
  }

  override def eraseAllByOrderId(orderId: Integer): Unit = {
    itemRepository.deleteAllByOrderId(orderId)
    itemRepository.flush()
  }
}

