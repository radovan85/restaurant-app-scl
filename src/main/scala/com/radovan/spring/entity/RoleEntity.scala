package com.radovan.spring.entity

import jakarta.persistence.{Column, Entity, FetchType, GeneratedValue, GenerationType, Id, ManyToMany, Table, Transient}
import org.springframework.security.core.GrantedAuthority

import java.util
import scala.beans.BeanProperty

@Entity
@Table(name = "roles")
@SerialVersionUID(1L)
class RoleEntity extends GrantedAuthority with Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @BeanProperty var id:Integer = _

  @Column(unique = true,nullable=false,length = 30)
  @BeanProperty var role:String = _

  @Transient
  @ManyToMany(mappedBy = "roles", fetch = FetchType.EAGER)
  @BeanProperty var users:util.List[UserEntity] = _

  def this(role: String) {
    this()
    this.role = role
  }

  override def getAuthority: String = role
}
