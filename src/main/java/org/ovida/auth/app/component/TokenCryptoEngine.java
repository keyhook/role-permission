package org.ovida.auth.app.component;

import org.ovida.auth.domain.model.Token;

public interface TokenCryptoEngine {

  String sign(Token token);

  Token verify(String encodedToken);
}
