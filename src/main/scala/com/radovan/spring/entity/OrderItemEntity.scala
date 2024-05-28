package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "ordered_items")
@SerialVersionUID(1L)
class OrderItemEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id:Integer = _

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

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "order_id", nullable = false)
  @BeanProperty var order:OrderEntity = _



}
