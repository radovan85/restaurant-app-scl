package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table, Transient}

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name="cart_items")
@SerialVersionUID(1L)
class CartItemEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id:Integer = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @Column(nullable = false)
  @BeanProperty var quantity:Integer = _

  @Column(name = "hotness_level", nullable = false)
  @BeanProperty var hotnessLevel:String = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "cart_id", nullable = false)
  @BeanProperty var cart:CartEntity = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "product_id", nullable = false)
  @BeanProperty var product:ProductEntity = _

  @Transient
  @BeanProperty var hotnessLevelList:util.List[String] = _
}
