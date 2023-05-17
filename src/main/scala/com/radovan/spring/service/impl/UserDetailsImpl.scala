package com.radovan.spring.service.impl

import com.radovan.spring.entity.UserEntity
import com.radovan.spring.exceptions.InvalidUserException
import com.radovan.spring.service.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

import java.util.Optional

@Service
class UserDetailsImpl extends UserDetailsService {

  @Autowired
  private val userService:UserService = null

  override def loadUserByUsername(name: String): UserEntity = {
    var returnValue:UserEntity = null
    val userOpt = Optional.ofNullable(userService.getUserByEmail(name))
    if (!userOpt.isPresent) {
      val error = new Error("Invalid user")
      throw new InvalidUserException(error)
    }
    else returnValue = userOpt.get
    returnValue
  }
}
