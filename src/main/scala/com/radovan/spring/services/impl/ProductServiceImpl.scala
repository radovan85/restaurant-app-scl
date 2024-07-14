package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.ProductDto
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repository.{CartItemRepository, ProductRepository}
import com.radovan.spring.services.{CartItemService, CartService, ProductService}
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import java.text.DecimalFormat
import scala.collection.JavaConverters._

@Service
class ProductServiceImpl extends ProductService{

  private var productRepository:ProductRepository = _
  private var tempConverter:TempConverter = _
  private var cartItemRepository:CartItemRepository = _
  private var cartItemService:CartItemService = _
  private var cartService:CartService = _
  private val decfor = new DecimalFormat("0.00")

  @Autowired
  private def injectAll(productRepository: ProductRepository,tempConverter: TempConverter,
                        cartItemRepository: CartItemRepository,cartService: CartService,
                        cartItemService: CartItemService):Unit = {

    this.productRepository = productRepository
    this.tempConverter = tempConverter
    this.cartItemRepository = cartItemRepository
    this.cartService = cartService
    this.cartItemService = cartItemService
  }

  @Transactional(readOnly = true)
  override def getProductById(productId: Integer): ProductDto = {
    val productEntity = productRepository.findById(productId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The product has not been found!")))
    tempConverter.productEntityToDto(productEntity)
  }

  @Transactional(readOnly = true)
  override def listAllByCategory(category: String): Array[ProductDto] = {
    val allProducts = productRepository.findAll().asScala
    allProducts.collect {
      case productEntity if productEntity.getCategory.equals(category) => tempConverter.productEntityToDto(productEntity)
    }.toArray
  }

  @Transactional
  override def addProduct(product: ProductDto): ProductDto = {
    val productIdOption = Option(product.getId)
    val storedProduct = productRepository.save(tempConverter.productDtoToEntity(product))
    val returnValue = tempConverter.productEntityToDto(storedProduct)
    productIdOption match {
      case Some(productId) =>
        val allCartItems = cartItemRepository.findAllByProductId(productId).asScala
        allCartItems.foreach(itemEntity => {
          val price = returnValue.getProductPrice * itemEntity.getQuantity
          itemEntity.setPrice(decfor.format(price).toFloat)
          cartItemRepository.saveAndFlush(itemEntity)
        })

        cartService.refreshAllCarts()
        returnValue
      case None => returnValue
    }
  }

  @Transactional(readOnly = true)
  override def listAll(): Array[ProductDto] = {
    val allProducts = productRepository.findAll().asScala
    allProducts.map(productEntity => tempConverter.productEntityToDto(productEntity)).toArray
  }

  @Transactional
  override def deleteProduct(productId: Integer): Unit = {
    getProductById(productId)
    cartItemService.eraseAllByProductId(productId)
    productRepository.deleteById(productId)
    productRepository.flush()
  }
}
