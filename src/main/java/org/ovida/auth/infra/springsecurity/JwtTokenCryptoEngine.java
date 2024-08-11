package org.ovida.auth.infra.springsecurity;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import java.nio.charset.StandardCharsets;
import java.time.ZoneOffset;
import java.util.*;
import javax.crypto.SecretKey;
import org.ovida.auth.app.component.TokenCryptoEngine;
import org.ovida.auth.domain.model.Token;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenCryptoEngine implements TokenCryptoEngine {

  private static final String ROLE = "rol";

  private static final String PRIVILEGE = "pri";

  private static final String EMAIL = "email";

  private final SecretKey secretKey;

  public JwtTokenCryptoEngine(SpringSecurityConfig springSecurityConfig) {
    this.secretKey = Keys.hmacShaKeyFor(Base64
      .getDecoder()
      .decode(springSecurityConfig.getJwtKey().getBytes(StandardCharsets.UTF_8)));
  }

  @Override
  public String sign(Token token) {
    return Jwts
      .builder()
      .id(token.getId().toString())
      .subject(token.getAccountId().toString())
      .claim(EMAIL, token.getEmail())
      .claim(ROLE, token.getRoleName())
      .claim(PRIVILEGE, token.getPrivilegeNames())
      .expiration(Date.from(token.getExpiredAt().toInstant(ZoneOffset.UTC)))
      .signWith(secretKey)
      .compact();
  }

  @Override
  public Token verify(String encodedToken) {
    var claims = Jwts
      .parser()
      .verifyWith(secretKey)
      .build()
      .parseSignedClaims(encodedToken)
      .getPayload();

    @SuppressWarnings("unchecked")
    var privilegeNames = new HashSet<String>(claims.get(PRIVILEGE, List.class));

    return new Token()
      .setId(UUID.fromString(claims.getId()))
      .setAccountId(UUID.fromString(claims.getSubject()))
      .setEmail(claims.get(EMAIL, String.class))
      .setRoleName(claims.get(ROLE, String.class))
      .setPrivilegeNames(privilegeNames)
      .setExpiredAt(claims.getExpiration().toInstant().atZone(ZoneOffset.UTC).toLocalDateTime());
  }
}
