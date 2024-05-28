package com.radovan.spring.services

import com.radovan.spring.dto.ProductDto

trait ProductService {

  def getProductById(productId:Integer):ProductDto
  def listAllByCategory(category:String):Array[ProductDto]
  def addProduct(product:ProductDto):ProductDto
  def listAll():Array[ProductDto]
  def deleteProduct(productId:Integer):Unit
}
