package com.radovan.spring.service

import java.util
import com.radovan.spring.dto.ProductDto

trait ProductService {

  def getProduct(productId: Integer): ProductDto

  def listByCategory(category: String): util.List[ProductDto]

  def addProduct(product: ProductDto): ProductDto

  def listAll: util.List[ProductDto]

  def deleteProduct(productId: Integer): Unit
}
