package com.radovan.spring.exceptions

import javax.management.RuntimeErrorException

@SerialVersionUID(1L)
class InvalidCartException(val e: Error) extends RuntimeErrorException(e) {

}

