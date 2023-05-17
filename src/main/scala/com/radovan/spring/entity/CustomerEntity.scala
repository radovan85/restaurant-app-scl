package com.radovan.spring.entity

import java.util
import jakarta.persistence.{CascadeType, Column, Entity, FetchType, GeneratedValue, GenerationType, Id, JoinColumn, OneToMany, OneToOne, Table}
import org.hibernate.annotations.{Fetch, FetchMode}

import scala.beans.BeanProperty

@Entity
@Table(name = "customers")
@SerialVersionUID(1L)
class CustomerEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @BeanProperty var customerId:Integer = _

  @Column(name = "phone", length = 15)
  @BeanProperty var customerPhone:String = _

  @OneToOne
  @JoinColumn(name = "address_id")
  @BeanProperty var address:AddressEntity = _

  @OneToOne(cascade = Array(CascadeType.MERGE), fetch = FetchType.EAGER)
  @JoinColumn(name = "user_id")
  @BeanProperty var user:UserEntity = _

  @OneToMany(mappedBy = "customer", cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  @BeanProperty var orders:util.List[OrderEntity] = _

  @OneToOne(cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @JoinColumn(name = "cart_id")
  @BeanProperty var cart:CartEntity = _

}

