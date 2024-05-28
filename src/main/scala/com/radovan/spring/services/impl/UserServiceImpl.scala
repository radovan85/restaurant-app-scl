package com.radovan.spring.services.impl

import com.radovan.spring.converter.TempConverter
import com.radovan.spring.dto.UserDto
import com.radovan.spring.entity.UserEntity
import com.radovan.spring.exceptions.InstanceUndefinedException
import com.radovan.spring.repository.UserRepository
import com.radovan.spring.services.UserService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

import scala.collection.JavaConverters._

@Service
class UserServiceImpl extends UserService{

  private var userRepository:UserRepository = _
  private var tempConverter:TempConverter = _

  @Autowired
  private def injectAll(userRepository: UserRepository,tempConverter: TempConverter):Unit = {
    this.userRepository = userRepository
    this.tempConverter = tempConverter
  }

  @Transactional(readOnly = true)
  override def getUserById(userId: Integer): UserDto = {
    val userEntity = userRepository.findById(userId)
      .orElseThrow(() => new InstanceUndefinedException(new Error("The user has not been found!")))
    tempConverter.userEntityToDto(userEntity)
  }

  @Transactional(readOnly = true)
  override def listAll(): Array[UserDto] = {
    val allUsers = userRepository.findAll().asScala
    allUsers.map(userEntity => tempConverter.userEntityToDto(userEntity)).toArray
  }

  @Transactional(readOnly = true)
  override def getUserByEmail(email: String): UserDto = {
    val userEntity = userRepository.findByEmail(email)
      .getOrElse(throw new InstanceUndefinedException(new Error("The user has not been found!")))
    tempConverter.userEntityToDto(userEntity)
  }

  @Transactional(readOnly = true)
  override def getCurrentUser(): UserDto = {
    val authUser = SecurityContextHolder.getContext.getAuthentication.getPrincipal.asInstanceOf[UserEntity]
    getUserById(authUser.getId)
  }
}
