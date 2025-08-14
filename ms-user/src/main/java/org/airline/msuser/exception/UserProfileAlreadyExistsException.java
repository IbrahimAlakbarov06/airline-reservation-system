package org.airline.msuser.exception;

public class UserProfileAlreadyExistsException extends RuntimeException {
  public UserProfileAlreadyExistsException(String message) {
    super(message);
  }
}
