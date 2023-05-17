package com.radovan.spring.controller

import com.radovan.spring.dto.ProductDto
import com.radovan.spring.service.ProductService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Controller
import org.springframework.ui.ModelMap
import org.springframework.web.bind.annotation.{GetMapping, PathVariable, RequestMapping}

import java.util

@Controller
@RequestMapping(value = Array("/products"))
class ProductController {

  @Autowired
  private val productService: ProductService = null

  @GetMapping(value = Array("/allProducts"))
  def allProductsList(map: ModelMap): String = {
    val allProducts: util.List[ProductDto] = productService.listAll
    val product: ProductDto = new ProductDto
    map.put("product", product)
    map.put("allProducts", allProducts)
    map.put("recordsPerPage", 9.asInstanceOf[Integer])
    "fragments/productList :: ajaxLoadedContent"
  }

  @GetMapping(value = Array("/allProductsByCategory/{category}"))
  def allProductsByCategory(@PathVariable("category") category: String, map: ModelMap): String = {
    val allProducts: util.List[ProductDto] = productService.listByCategory(category)
    val product: ProductDto = new ProductDto
    map.put("product", product)
    map.put("allProducts", allProducts)
    "fragments/productList :: ajaxLoadedContent"
  }
}

