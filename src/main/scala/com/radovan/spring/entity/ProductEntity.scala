package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, Table, Transient}

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "products")
@SerialVersionUID(1L)
class ProductEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id:Integer = _

  @Column(name="product_name",nullable = false,length = 40)
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
