package org.ovida.auth.domain.exception;

import lombok.Getter;

@Getter
public enum DomainCode {

  // Success (start with 0)
  OK("0000"),

  // Error (start with 1)
  ERROR_UNKNOWN("1000"),

  INPUT_FIELD_INVALID("1001"),

  REQUEST_PARAMETER_MISSED("1002"),

  HTTP_MESSAGE_NOT_READABLE("1003"),

  METHOD_ARGUMENT_TYPE_MISMATCH("1004"),

  HTTP_MEDIA_TYPE_NOT_SUPPORTED("1005"),

  HTTP_REQUEST_METHOD_NOT_SUPPORTED("1006"),

  REQUEST_HEADER_MISSED("1007"),

  NUMBER_FORMAT_INVALID("1008"),

  UNAUTHORIZED("1009"),

  ENUM_INVALID("1010"),

  FORBIDDEN("1011"),

  // Business logic error (start with 11)
  ENTITY_NOT_FOUND("1100"),

  ENTITY_INVALID_STATUS("1101"),

  ENTITY_ALREADY_EXISTS("1102"),

  CREDENTIALS_INVALID("1103"),

  TOKEN_INVALID("1104"),

  TOKEN_EXPIRED("1105");

  private final String value;

  DomainCode(String value) {
    this.value = value;
  }

  public String getUniversalCode() {
    return "OVI%s".formatted(value);
  }
}
