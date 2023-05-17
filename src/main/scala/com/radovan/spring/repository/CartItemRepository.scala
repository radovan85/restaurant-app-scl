package com.radovan.spring.repository

import java.util
import com.radovan.spring.entity.CartItemEntity
import org.springframework.data.jpa.repository.{JpaRepository, Modifying, Query}
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
trait CartItemRepository extends JpaRepository[CartItemEntity, Integer] {

  @Query(value = "select * from cart_items where cart_id = :cartId", nativeQuery = true)
  def findAllByCartId(@Param("cartId") cartId: Integer): util.List[CartItemEntity]

  @Query(value = "select * from cart_items where product_id = :productId", nativeQuery = true)
  def findAllByProductId(@Param("productId") productId: Integer): util.List[CartItemEntity]

  @Query(value = "select sum(price) from cart_items where cart_id = :cartId", nativeQuery = true)
  def calculateGrandTotal(@Param("cartId") cartId: Integer): java.lang.Float

  @Modifying
  @Query(value = "delete from cart_items where cart_id = :cartId", nativeQuery = true)
  def removeAllByCartId(@Param("cartId") cartId: Integer): Unit

  @Modifying
  @Query(value = "delete from cart_items where item_id = :itemId", nativeQuery = true)
  def removeCartItem(@Param("itemId") itemId: Integer): Unit

  @Query(value = "select * from cart_items where cart_id = :cartId and product_id = :productId and hotness_level = :hotnessLevel", nativeQuery = true)
  def findByCartIdAndProductIdAndHotnessLevel(@Param("cartId") cartId: Integer, @Param("productId") productId: Integer, @Param("hotnessLevel") hotnessLevel: String): CartItemEntity

  @Modifying
  @Query(value = "delete from cart_items where product_id = :productId", nativeQuery = true)
  def removeAllByProductId(@Param("productId") productId: Integer): Unit
}

