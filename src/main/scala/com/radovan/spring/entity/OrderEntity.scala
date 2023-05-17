package com.radovan.spring.entity

import java.util
import jakarta.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, ManyToOne, OneToMany, OneToOne, Table}
import org.hibernate.annotations.{Fetch, FetchMode}

import java.sql.Timestamp
import scala.beans.BeanProperty

@Entity
@Table(name = "orders")
@SerialVersionUID(1L)
class OrderEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "order_id")
  @BeanProperty var orderId:Integer = _

  @ManyToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "address_id")
  @BeanProperty var address:OrderAddressEntity = _

  @OneToMany(mappedBy = "order", cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  @BeanProperty var orderItems:util.List[OrderItemEntity] = _

  @Column(nullable = false)
  @BeanProperty var price:Float = _

  @Column(nullable = false, name = "created_at")
  @BeanProperty var createdAt:Timestamp = _

}

