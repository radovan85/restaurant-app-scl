package com.radovan.spring.service.impl

import java.util
import java.util.Optional
import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.CartItemDto
import com.radovan.spring.entity.{CartEntity, CartItemEntity}
import com.radovan.spring.repository.{CartItemRepository, CartRepository}
import com.radovan.spring.service.{CartItemService, CartService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional




@Service
@Transactional
class CartItemServiceImpl extends CartItemService {

  @Autowired
  private val cartItemRepository:CartItemRepository = null

  @Autowired
  private val tempConverter:TempConverter = null

  @Autowired
  private val cartService:CartService = null

  @Autowired
  private val cartRepository:CartRepository = null

  override def addCartItem(cartItem: CartItemDto): CartItemDto = {
    val cartItemEntity = tempConverter.cartItemDtoToEntity(cartItem)
    val storedItem = cartItemRepository.save(cartItemEntity)
    val returnValue = tempConverter.cartItemEntityToDto(storedItem)
    returnValue
  }

  override def removeCartItem(cartId: Integer, itemId: Integer): Unit = {
    cartItemRepository.removeCartItem(itemId)
    cartItemRepository.flush()
    cartService.refreshCartState(cartId)
  }

  override def eraseAllCartItems(cartId: Integer): Unit = {
    cartItemRepository.removeAllByCartId(cartId)
    cartItemRepository.flush()
    cartService.refreshCartState(cartId)
  }

  override def listAllByCartId(cartId: Integer): util.List[CartItemDto] = {
    val cartItemsOpt = Optional.ofNullable(cartItemRepository.findAllByCartId(cartId))
    val returnValue = new util.ArrayList[CartItemDto]
    if (!cartItemsOpt.isEmpty) cartItemsOpt.get.forEach((item: CartItemEntity) => {
      def foo(item: CartItemEntity) = {
        val itemDto = tempConverter.cartItemEntityToDto(item)
        returnValue.add(itemDto)
      }

      foo(item)
    })
    returnValue
  }

  override def getCartItem(id: Integer): CartItemDto = {
    val cartItemOpt = cartItemRepository.findById(id)
    var returnValue:CartItemDto = null
    if (cartItemOpt.isPresent) returnValue = tempConverter.cartItemEntityToDto(cartItemOpt.get)
    returnValue
  }

  override def getCartItemByCartIdAndProductIdAndHotnessLevel(cartId: Integer, productId: Integer, hotnessLevel: String): CartItemDto = {
    val cartItemOpt = Optional.ofNullable(cartItemRepository.findByCartIdAndProductIdAndHotnessLevel(cartId, productId, hotnessLevel))
    var returnValue:CartItemDto = null
    if (cartItemOpt.isPresent) returnValue = tempConverter.cartItemEntityToDto(cartItemOpt.get)
    returnValue
  }

  override def eraseAllByProductId(productId: Integer): Unit = {
    cartItemRepository.removeAllByProductId(productId)
    cartItemRepository.flush()
    val allCartsOpt: Optional[util.List[CartEntity]] = Optional.ofNullable(cartRepository.findAll)
    if (!allCartsOpt.isEmpty) allCartsOpt.get.forEach((cartEntity: CartEntity) => {
      def foo(cartEntity: CartEntity): Unit = cartService.refreshCartState(cartEntity.getCartId)

      foo(cartEntity)
    })
  }
}

