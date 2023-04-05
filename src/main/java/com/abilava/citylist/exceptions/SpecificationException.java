package com.abilava.citylist.exceptions;

import org.springframework.http.HttpStatus;

public class SpecificationException extends ServerException {

  public SpecificationException(String message) {
    super(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }
}
