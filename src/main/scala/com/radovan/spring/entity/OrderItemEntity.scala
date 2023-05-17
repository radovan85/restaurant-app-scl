package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "order_items")
@SerialVersionUID(1L)
class OrderItemEntity extends Serializable {

  @Column(name = "item_id")
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var orderItemId:Integer = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @Column(nullable = false)
  @BeanProperty var quantity:Integer = _

  @Column(name = "hotness_level", nullable = false)
  @BeanProperty var hotnessLevel:String = _

  @Column(name = "product_name", length = 40, nullable = false)
  @BeanProperty var productName:String = _

  @Column(name = "product_price", nullable = false)
  @BeanProperty var productPrice:Float = _

  @ManyToOne
  @JoinColumn(name = "order_id")
  @BeanProperty var order:OrderEntity = _


}

