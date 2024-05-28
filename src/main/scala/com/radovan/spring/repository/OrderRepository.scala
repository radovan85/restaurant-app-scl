package com.radovan.spring.repository

import com.radovan.spring.entity.OrderEntity
import org.springframework.data.jpa.repository.{JpaRepository, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository
import java.util

@Repository
trait OrderRepository extends JpaRepository[OrderEntity, Integer]{

  @Query(value = "select * from orders where cart_id = :cartId", nativeQuery = true)
  def findAllByCartId(@Param("cartId") cartId:Integer):util.List[OrderEntity]

}
