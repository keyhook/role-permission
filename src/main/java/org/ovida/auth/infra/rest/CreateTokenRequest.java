package org.ovida.auth.infra.rest;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@ToString(exclude = "password")
public class CreateTokenRequest {

  @NotBlank
  @Email
  private String email;

  @NotBlank
  private String password;
}
