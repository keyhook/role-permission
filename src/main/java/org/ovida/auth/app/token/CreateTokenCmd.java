package org.ovida.auth.app.token;

import lombok.Data;
import lombok.ToString;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@Data
@ToString(exclude = "password")
public class CreateTokenCmd {

  private String email;

  private String password;
}
