package com.abilava.citylist.exceptions;

import org.springframework.http.HttpStatus;

public class NotFoundException extends ServerException {

  public NotFoundException(String message) {
    super(message, HttpStatus.NOT_FOUND);
  }
}
