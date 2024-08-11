package org.ovida.auth.infra.rest;

import static org.ovida.auth.domain.exception.DomainCode.OK;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@ControllerAdvice(basePackageClasses = RestMarker.class)
public class ApiRestWrapper implements ResponseBodyAdvice<Object> {

  private final MessageSource messageSource;

  public ApiRestWrapper(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  @Override
  public Object beforeBodyWrite(
    Object body,
    MethodParameter returnType,
    MediaType selectedContentType,
    Class<? extends HttpMessageConverter<?>> selectedConverterType,
    ServerHttpRequest request,
    ServerHttpResponse response) {
    if (body instanceof ApiResp) {
      return body;
    }

    return new ApiResp()
      .setCode(OK.getUniversalCode())
      .setMsg(messageSource.getMessage(OK.getValue(), null, LocaleContextHolder.getLocale()))
      .setData(body);
  }

  @Override
  public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
    return true;
  }
}
