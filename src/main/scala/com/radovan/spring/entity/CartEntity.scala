package com.radovan.spring.entity

import jakarta.persistence._
import org.hibernate.annotations.{Fetch, FetchMode}

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "carts")
@SerialVersionUID(1L)
class CartEntity extends Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  @Column(name = "id")
  @BeanProperty var cartId:Integer = _

  @OneToOne
  @JoinColumn(name = "customer_id")
  @BeanProperty var customer:CustomerEntity = _

  @OneToMany(mappedBy = "cart", cascade = Array(CascadeType.ALL), fetch = FetchType.EAGER)
  @Fetch(value = FetchMode.SUBSELECT)
  @BeanProperty var cartItems:util.List[CartItemEntity] = _

  @Column(name = "cart_price")
  @BeanProperty var cartPrice:Float = _

}

