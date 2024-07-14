package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToOne, Table}

import scala.beans.BeanProperty


@Entity
@Table(name = "customers")
@SerialVersionUID(1L)
class CustomerEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id:Integer = _

  @Column(name = "customer_phone",nullable = false,length = 15)
  @BeanProperty var customerPhone:String = _

  @OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
  @JoinColumn(name = "address_id", nullable = false)
  @BeanProperty var address:AddressEntity = _

  @OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
  @JoinColumn(name = "user_id",nullable = false)
  @BeanProperty var user:UserEntity = _

  @OneToOne(fetch = FetchType.EAGER,orphanRemoval = true)
  @JoinColumn(name="cart_id",nullable = false)
  @BeanProperty var cart:CartEntity = _
}
