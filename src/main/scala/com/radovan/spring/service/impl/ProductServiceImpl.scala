package com.radovan.spring.service.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.ProductDto
import com.radovan.spring.entity.{CartEntity, CartItemEntity, ProductEntity}
import com.radovan.spring.repository.{CartItemRepository, CartRepository, ProductRepository}
import com.radovan.spring.service.{CartService, ProductService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.text.DecimalFormat
import java.util
import java.util.Optional

@Service
@Transactional
class ProductServiceImpl extends ProductService {

  @Autowired
  private val productRepository: ProductRepository = null

  @Autowired
  private val tempConverter: TempConverter = null

  @Autowired
  private val cartService: CartService = null

  @Autowired
  private val cartRepository: CartRepository = null

  @Autowired
  private val cartItemRepository: CartItemRepository = null

  private val decfor: DecimalFormat = new DecimalFormat("0.00")

  override def getProduct(productId: Integer): ProductDto = {
    val productOpt: Optional[ProductEntity] = productRepository.findById(productId)
    var returnValue: ProductDto = null
    if (productOpt.isPresent) returnValue = tempConverter.productEntityToDto(productOpt.get)
    returnValue
  }

  override def listByCategory(category: String): util.List[ProductDto] = {
    val allProductsOpt: Optional[util.List[ProductEntity]] = Optional.ofNullable(productRepository.findAllByCategory(category))
    val returnValue: util.List[ProductDto] = new util.ArrayList[ProductDto]
    if (!allProductsOpt.isEmpty) allProductsOpt.get.forEach((product: ProductEntity) => {
      def foo(product: ProductEntity) = {
        val productDto: ProductDto = tempConverter.productEntityToDto(product)
        returnValue.add(productDto)
      }

      foo(product)
    })
    returnValue
  }

  override def addProduct(product: ProductDto): ProductDto = {
    val productIdOpt: Optional[Integer] = Optional.ofNullable(product.getProductId)
    val productEntity: ProductEntity = tempConverter.productDtoToEntity(product)
    val storedProduct: ProductEntity = productRepository.save(productEntity)
    val returnValue: ProductDto = tempConverter.productEntityToDto(storedProduct)
    if (productIdOpt.isPresent) {
      val allCartItems: Optional[util.List[CartItemEntity]] = Optional.ofNullable(cartItemRepository.findAllByProductId(returnValue.getProductId))
      if (!allCartItems.isEmpty) {
        allCartItems.get.forEach((itemEntity: CartItemEntity) => {
          def foo(itemEntity: CartItemEntity) = {
            var price: Float = returnValue.getProductPrice
            price = price * itemEntity.getQuantity
            price = decfor.format(price).toFloat
            itemEntity.setPrice(price)
            cartItemRepository.saveAndFlush(itemEntity)
          }

          foo(itemEntity)
        })
        val allCartsOpt: Optional[util.List[CartEntity]] = Optional.ofNullable(cartRepository.findAll)
        if (!allCartsOpt.isEmpty) allCartsOpt.get.forEach((cartEntity: CartEntity) => {
          def foo(cartEntity: CartEntity): Unit = {
            cartService.refreshCartState(cartEntity.getCartId)
          }

          foo(cartEntity)
        })
      }
    }
    returnValue
  }

  override def listAll: util.List[ProductDto] = {
    val allProductsOpt: Optional[util.List[ProductEntity]] = Optional.ofNullable(productRepository.findAll)
    val returnValue: util.List[ProductDto] = new util.ArrayList[ProductDto]
    if (!allProductsOpt.isEmpty) allProductsOpt.get.forEach((productEntity: ProductEntity) => {
      def foo(productEntity: ProductEntity) = {
        val productDto: ProductDto = tempConverter.productEntityToDto(productEntity)
        returnValue.add(productDto)
      }

      foo(productEntity)
    })
    returnValue
  }

  override def deleteProduct(productId: Integer): Unit = {
    productRepository.deleteById(productId)
    productRepository.flush()
  }
}

