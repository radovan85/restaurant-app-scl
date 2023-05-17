package com.radovan.spring.repository

import java.util
import com.radovan.spring.entity.ProductEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait ProductRepository extends JpaRepository[ProductEntity, Integer] {

  @Query(value = "select * from products where category = :category", nativeQuery = true)
  def findAllByCategory(@Param("category") category: String): util.List[ProductEntity]

}
