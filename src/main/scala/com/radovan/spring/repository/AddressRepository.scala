package com.radovan.spring.repository

import com.radovan.spring.entity.AddressEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
trait AddressRepository extends JpaRepository[AddressEntity, Integer] {

}