package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, OneToOne, Table}

import java.util
import java.sql.Timestamp
import scala.beans.BeanProperty

@Entity
@Table(name = "orders")
@SerialVersionUID(1L)
class OrderEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id:Integer = _

  @Column(name = "order_price", nullable = false)
  @BeanProperty var orderPrice:Float = _

  @ManyToOne(fetch = FetchType.EAGER)
  @JoinColumn(name = "cart_id",nullable = false)
  @BeanProperty var cart:CartEntity = _

  @Column(name="created_at",nullable = false)
  @BeanProperty var createdAt:Timestamp = _

  @OneToMany(fetch = FetchType.EAGER,orphanRemoval = true)
  @BeanProperty var orderedItems:util.List[OrderItemEntity] = _

  @OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
  @JoinColumn(name = "address_id", nullable = false)
  @BeanProperty var address:OrderAddressEntity = _

}
