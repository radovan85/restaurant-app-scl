package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, GeneratedValue, GenerationType, Id, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name = "addresses")
@SerialVersionUID(1L)
class AddressEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var addressId:Integer = _

  @Column(nullable = false, length = 75)
  @BeanProperty var address:String = _

  @Column(nullable = false, length = 40)
  @BeanProperty var city:String = _

  @Column(name = "post_code", nullable = false, length = 10)
  @BeanProperty var postcode:String = _

  @OneToOne(mappedBy = "address")
  @BeanProperty var customer:CustomerEntity = _


}

