package org.example.canicampusconnectapi.common.exception;

import org.springframework.security.core.AuthenticationException;

public class EmailNotValidatedException extends AuthenticationException {
  public EmailNotValidatedException(String message) {
    super(message);
  }
}