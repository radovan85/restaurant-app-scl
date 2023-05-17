package com.radovan.spring.controller

import com.radovan.spring.exceptions.{ExistingEmailException, InvalidCartException, InvalidUserException}
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler}

@ControllerAdvice
class ExceptionController {

  @ExceptionHandler(Array(classOf[ExistingEmailException]))
  def handleExistingEmailException(): ResponseEntity[String] = ResponseEntity.internalServerError.body("Email exists already!")

  @ExceptionHandler(Array(classOf[InvalidUserException]))
  def handleInvalidUserException(): ResponseEntity[String] = ResponseEntity.internalServerError.body("Invalid user!")

  @ExceptionHandler(Array(classOf[InvalidCartException]))
  def handleInvalidCartException(): ResponseEntity[String] = ResponseEntity.internalServerError.body("Invalid cart!")
}
