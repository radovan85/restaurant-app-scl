package com.radovan.spring.controller

import com.radovan.spring.exceptions.{InstanceAlreadyExistsException, InstanceUndefinedException, InvalidCartException}
import org.springframework.http.{HttpStatus, ResponseEntity}
import org.springframework.web.bind.annotation.{ControllerAdvice, ExceptionHandler}

@ControllerAdvice
class ErrorsController {

  @ExceptionHandler(Array(classOf[InstanceAlreadyExistsException]))
  def handleInstanceAlreadyExistsException(error: Error): ResponseEntity[String] = new ResponseEntity[String](error.getMessage, HttpStatus.CONFLICT)

  @ExceptionHandler(Array(classOf[InstanceUndefinedException]))
  def handleInstanceUndefinedException(error: Error): ResponseEntity[String] = new ResponseEntity[String](error.getMessage, HttpStatus.UNPROCESSABLE_ENTITY)

  @ExceptionHandler(Array(classOf[InvalidCartException]))
  def handleInvalidCartException(error:Error): ResponseEntity[String] = new ResponseEntity[String](error.getMessage, HttpStatus.UNPROCESSABLE_ENTITY)
}
