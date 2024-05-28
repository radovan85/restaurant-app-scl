package com.radovan.spring.services

import com.radovan.spring.dto.CustomerDto
import com.radovan.spring.utils.RegistrationForm

trait CustomerService {

  def addCustomer(customer:CustomerDto):CustomerDto
  def getCustomerById(customerId:Integer):CustomerDto
  def getCustomerByUserId(userId:Integer):CustomerDto
  def listAll():Array[CustomerDto]
  def registerCustomer(form:RegistrationForm):CustomerDto
  def getCustomerByCartId(cartId:Integer):CustomerDto
  def deleteCustomer(customerId:Integer):Unit
  def getCurrentCustomer():CustomerDto
}
