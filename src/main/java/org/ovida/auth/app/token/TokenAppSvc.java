package org.ovida.auth.app.token;

public interface TokenAppSvc {

  String create(CreateTokenCmd cmd);
}
