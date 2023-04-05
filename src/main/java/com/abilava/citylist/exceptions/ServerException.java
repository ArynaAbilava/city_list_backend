package com.abilava.citylist.exceptions;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

@Getter
@FieldDefaults(level = AccessLevel.PRIVATE, makeFinal = true)
public class ServerException extends RuntimeException {

  HttpStatus status;

  public ServerException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }
}
