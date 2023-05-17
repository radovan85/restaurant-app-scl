package com.radovan.spring.entity

import java.util
import jakarta.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, Table, Transient}

import scala.beans.BeanProperty

@Entity
@Table(name = "products")
@SerialVersionUID(1L)
class ProductEntity extends Serializable {

  @Column(name = "product_id")
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Id
  @BeanProperty var productId:Integer = _

  @Column(name = "product_name", length = 40, nullable = false)
  @BeanProperty var productName:String = _

  @Column(name = "product_price", nullable = false)
  @BeanProperty var productPrice:Float = _

  @Column(nullable = false, length = 90)
  @BeanProperty var description:String = _

  @Column(nullable = false)
  @BeanProperty var category:String = _

  @Transient
  @BeanProperty var categoryList:util.List[String] = _

  @Column(name = "image_url", nullable = false, length = 255)
  @BeanProperty var imageUrl:String = _

}

