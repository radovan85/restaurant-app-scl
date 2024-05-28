package com.radovan.spring.controller

import com.radovan.spring.services.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import com.radovan.spring.dto.ProductDto



@Controller
@RequestMapping(value = Array("/products"))
class ProductController {

  @Autowired
  private var productService:ProductService = _

  @GetMapping(value = Array("/allProducts"))
  def allProductsList(map: ModelMap): String = {
    val allProducts = productService.listAll()
    val product = new ProductDto
    map.put("product", product)
    map.put("allProducts", allProducts)
    map.put("recordsPerPage", 9.asInstanceOf[Integer])
    "fragments/productList :: fragmentContent"
  }

  @GetMapping(value = Array("/allProductsByCategory/{category}"))
  def allProductsByCategory(@PathVariable("category") category: String, map: ModelMap): String = {
    val allProducts = productService.listAllByCategory(category)
    val product = new ProductDto
    map.put("product", product)
    map.put("allProducts", allProducts)
    "fragments/productList :: fragmentContent"
  }
}
