package org.ovida.auth.infra.springsecurity;

import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ovida.auth.app.component.TokenCryptoEngine;
import org.ovida.auth.domain.model.Token;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

@Slf4j
@RequiredArgsConstructor
public class AuthTokenFilter extends OncePerRequestFilter {

  private static final String BEARER_SCHEME = "Bearer ";

  private static final String AUTHORIZATION = "Authorization";

  private final TokenCryptoEngine tokenCryptoEngine;

  private UsernamePasswordAuthenticationToken createAuth(Token token) {
    // WARNING: authorization via stateless JWT won't take effect until the token regeneration. If we need immediate
    // effect, stateful approach is inevitable.
    var authorities = token
      .getPrivilegeNames()
      .stream()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toSet());

    return UsernamePasswordAuthenticationToken.authenticated(
      token,
      null,
      authorities);
  }

  @Override
  protected void doFilterInternal(
    HttpServletRequest request,
    HttpServletResponse response,
    FilterChain filterChain) throws ServletException, IOException {
    try {
      var token = tokenCryptoEngine.verify(request.getHeader(AUTHORIZATION).substring(BEARER_SCHEME.length()));

      SecurityContextHolder
        .getContext()
        .setAuthentication(createAuth(token));
    } catch (ExpiredJwtException e) {
      log.warn("method: doFilterInternal", e);

      request.setAttribute("expired", true);
    } catch (Exception e) {
      log.warn("method: doFilterInternal", e);

      request.removeAttribute("expired");
    }

    filterChain.doFilter(request, response);
  }
}
