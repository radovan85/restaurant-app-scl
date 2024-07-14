package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, OneToOne, Table}

import scala.beans.BeanProperty

@Entity
@Table(name="addresses")
@SerialVersionUID(1L)
class AddressEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id:Integer = _

  @Column(nullable = false, length = 75)
  @BeanProperty var address:String = _

  @Column(nullable = false, length = 40)
  @BeanProperty var city:String = _

  @Column(nullable = false, length = 10)
  @BeanProperty var postcode:String = _

  @OneToOne(mappedBy = "address",fetch = FetchType.EAGER)
  @BeanProperty var customer:CustomerEntity = _
}
