package org.ovida.auth.infra.springsecurity;

import static org.ovida.auth.domain.exception.DomainCode.FORBIDDEN;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.ovida.auth.infra.rest.ApiResp;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ForbiddenRequestHandler implements AccessDeniedHandler {

  private final MessageSource messageSource;

  private final ObjectMapper objectMapper;

  public ForbiddenRequestHandler(
    MessageSource messageSource,
    ObjectMapper objectMapper) {
    this.messageSource = messageSource;
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(
    HttpServletRequest request,
    HttpServletResponse response,
    AccessDeniedException accessDeniedException) throws IOException {
    log.warn("method: handle - endpoint: {} - queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      accessDeniedException);

    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.getWriter().write(objectMapper.writeValueAsString(new ApiResp()
      .setCode(FORBIDDEN.getUniversalCode())
      .setMsg(messageSource.getMessage(FORBIDDEN.getValue(), null, LocaleContextHolder.getLocale()))));
  }
}
