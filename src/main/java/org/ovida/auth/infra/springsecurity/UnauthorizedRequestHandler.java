package org.ovida.auth.infra.springsecurity;

import static org.ovida.auth.domain.exception.DomainCode.TOKEN_EXPIRED;
import static org.ovida.auth.domain.exception.DomainCode.UNAUTHORIZED;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.ovida.auth.infra.rest.ApiResp;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class UnauthorizedRequestHandler implements AuthenticationEntryPoint {

  private final MessageSource messageSource;

  private final ObjectMapper objectMapper;

  public UnauthorizedRequestHandler(
    MessageSource messageSource,
    ObjectMapper objectMapper) {
    this.messageSource = messageSource;
    this.objectMapper = objectMapper;
  }

  private ApiResp getApiResp(HttpServletRequest request) {
    return Optional
      .ofNullable(request.getAttribute("expired"))
      .map(expired -> new ApiResp()
        .setCode(TOKEN_EXPIRED.getUniversalCode())
        .setMsg(messageSource.getMessage(TOKEN_EXPIRED.getValue(), null, LocaleContextHolder.getLocale())))
      .orElseGet(() -> new ApiResp()
        .setCode(UNAUTHORIZED.getUniversalCode())
        .setMsg(messageSource.getMessage(UNAUTHORIZED.getValue(), null, LocaleContextHolder.getLocale())));
  }

  @Override
  public void commence(
    HttpServletRequest request,
    HttpServletResponse response,
    AuthenticationException authException) throws IOException {
    log.warn("method: commence - endpoint: {} - queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      authException);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(objectMapper.writeValueAsString(getApiResp(request)));
  }
}
