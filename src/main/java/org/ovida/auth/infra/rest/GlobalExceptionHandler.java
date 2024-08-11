package org.ovida.auth.infra.rest;

import static org.ovida.auth.domain.exception.DomainCode.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.ovida.auth.domain.exception.DomainException;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Once defining a specific exception to be caught in this global exception handler, it will apply to exception thrown
 * from <b>any</b> controller.
 *
 * <p><b>Note:</b>
 * <ul>
 * <li>{@link ExceptionHandler @ExceptionHandler} methods on the {@code Controller} are always
 * selected before those on this handler.
 * <li>If the exception is annotated with {@link ResponseStatus @ResponseStatus}, rethrow it and
 * let the framework handle it. For example:
 * <pre>
 *   if (AnnotationUtils.findAnnotation(exception.getClass(), ResponseStatus.class) != null) {
 *     throw exception;
 *   }
 * </pre>
 * </ul>
 */
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

  /**
   * Responsible for finding the appropriate error messages based on the given code & locale.
   */
  private final MessageSource messageSource;

  public GlobalExceptionHandler(MessageSource messageSource) {
    this.messageSource = messageSource;
  }

  /**
   * Handles {@link MethodArgumentNotValidException}.
   *
   * <p><b>NOTE:</b> This exception handler is mainly used for POST method request with a JSON
   * request body.
   */
  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ApiResp methodArgumentNotValidException(HttpServletRequest request, MethodArgumentNotValidException e) {
    var errorDetails = e
      .getBindingResult()
      .getFieldErrors()
      .stream()
      .map(this::toErrorDetail)
      .toList();

    log.warn(
      "method: methodArgumentNotValidException - endPoint: {} - queryString: {} - errorDetails: {}",
      request.getRequestURI(),
      request.getQueryString(),
      errorDetails,
      e);

    return new ApiResp()
      .setCode(INPUT_FIELD_INVALID.getUniversalCode())
      .setMsg(messageSource.getMessage(INPUT_FIELD_INVALID.getValue(), null, LocaleContextHolder.getLocale()))
      .setData(Map.of("errorDetails", errorDetails));
  }

  /**
   * Handles {@link BindException}.
   *
   * <p><b>NOTE:</b> This exception handler is mainly used for GET method request with standard
   * query parameters.
   */
  @ExceptionHandler(BindException.class)
  public ApiResp bindException(HttpServletRequest request, BindException e) {
    var errorDetails = e
      .getFieldErrors()
      .stream()
      .map(this::toErrorDetail)
      .toList();

    log.warn(
      "method: bindException, endPoint: {} - queryString: {} - errorDetails: {}",
      request.getRequestURI(),
      request.getQueryString(),
      errorDetails,
      e);

    return new ApiResp()
      .setCode(INPUT_FIELD_INVALID.getUniversalCode())
      .setMsg(messageSource.getMessage(INPUT_FIELD_INVALID.getValue(), null, LocaleContextHolder.getLocale()))
      .setData(Map.of("errorDetails", errorDetails));
  }

  private Map<String, Object> toErrorDetail(FieldError fieldError) {
    var errMsg = fieldError.getDefaultMessage();
    if (errMsg.startsWith("Failed to convert property value of type")) {
      errMsg = messageSource.getMessage("ovida.invalid_data_type", null, LocaleContextHolder.getLocale());
    }
    return Map.of(
      "propertyName", fieldError.getField(),
      "msg", errMsg);
  }

  @ExceptionHandler(MissingServletRequestParameterException.class)
  public ApiResp missingServletRequestParameterException(HttpServletRequest request,
    MissingServletRequestParameterException e) {
    log.warn(
      "method: missingServletRequestParameterException - endPoint: {} - queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      e);

    return new ApiResp()
      .setCode(REQUEST_PARAMETER_MISSED.getUniversalCode())
      .setMsg(messageSource.getMessage(
        REQUEST_PARAMETER_MISSED.getUniversalCode(),
        new Object[]{e.getParameterName()},
        LocaleContextHolder.getLocale()));
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ApiResp httpMessageNotReadableException(HttpServletRequest request, HttpMessageNotReadableException e) {
    log.warn(
      "method: httpMessageNotReadableException - endPoint: {} - queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      e);

    if (Objects.nonNull(e.getCause()) &&
      e.getCause().getCause() instanceof DomainException cause) {
      return new ApiResp()
        .setCode(cause.getDomainCode().getUniversalCode())
        .setMsg(messageSource.getMessage(
          cause.getDomainCode().getValue(),
          cause.getArgs(),
          LocaleContextHolder.getLocale()));
    }

    return new ApiResp()
      .setCode(HTTP_MESSAGE_NOT_READABLE.getUniversalCode())
      .setMsg(messageSource.getMessage(
        HTTP_MESSAGE_NOT_READABLE.getValue(),
        null,
        LocaleContextHolder.getLocale()));
  }

  @ExceptionHandler(MethodArgumentTypeMismatchException.class)
  public ApiResp methodArgumentTypeMismatchException(HttpServletRequest request,
    MethodArgumentTypeMismatchException e) {
    log.warn(
      "method: methodArgumentTypeMismatchException - endPoint: {} - queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      e);

    return new ApiResp()
      .setCode(METHOD_ARGUMENT_TYPE_MISMATCH.getUniversalCode())
      .setMsg(
        messageSource.getMessage(
          METHOD_ARGUMENT_TYPE_MISMATCH.getValue(),
          new Object[]{e.getName()},
          LocaleContextHolder.getLocale()));
  }

  @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
  public ApiResp httpMediaTypeNotSupportedException(HttpServletRequest request, HttpMediaTypeNotSupportedException e) {
    log.warn(
      "method: httpMediaTypeNotSupportedException - endPoint: {} - queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      e);

    return new ApiResp()
      .setCode(HTTP_MEDIA_TYPE_NOT_SUPPORTED.getUniversalCode())
      .setMsg(
        messageSource.getMessage(
          HTTP_MEDIA_TYPE_NOT_SUPPORTED.getValue(),
          new Object[]{e.getContentType()},
          LocaleContextHolder.getLocale()));
  }

  @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
  public ApiResp httpRequestMethodNotSupportedException(HttpServletRequest request,
    HttpRequestMethodNotSupportedException e) {
    log.warn(
      "method: httpRequestMethodNotSupportedException - endPoint: {} - queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      e);

    return new ApiResp()
      .setCode(HTTP_REQUEST_METHOD_NOT_SUPPORTED.getUniversalCode())
      .setMsg(
        messageSource.getMessage(
          HTTP_REQUEST_METHOD_NOT_SUPPORTED.getValue(),
          new Object[]{e.getMethod()},
          LocaleContextHolder.getLocale()));
  }

  @ExceptionHandler(MissingRequestHeaderException.class)
  public ApiResp missingRequestHeaderException(HttpServletRequest request, MissingRequestHeaderException e) {
    log.warn(
      "method: missingRequestHeaderException - endPoint: {} - queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      e
    );

    return new ApiResp()
      .setCode(REQUEST_HEADER_MISSED.getUniversalCode())
      .setMsg(
        messageSource.getMessage(
          REQUEST_HEADER_MISSED.getValue(),
          new Object[]{e.getHeaderName()},
          LocaleContextHolder.getLocale()));
  }

  @ExceptionHandler(NumberFormatException.class)
  public ApiResp numberFormatException(HttpServletRequest request, NumberFormatException e) {
    log.warn(
      "method: numberFormatException - endPoint: {} - queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      e);

    return new ApiResp()
      .setCode(NUMBER_FORMAT_INVALID.getUniversalCode())
      .setMsg(messageSource.getMessage(
        NUMBER_FORMAT_INVALID.getValue(),
        new Object[]{e.getMessage()},
        LocaleContextHolder.getLocale()));
  }

  @ExceptionHandler(DomainException.class)
  public ApiResp domainException(HttpServletRequest request, DomainException e) {
    log.warn(
      "method: domainException, endPoint: {} , queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      e);

    return new ApiResp()
      .setCode(e.getDomainCode().getUniversalCode())
      .setMsg(messageSource.getMessage(e.getDomainCode().getValue(), e.getArgs(), LocaleContextHolder.getLocale()));
  }

  @ExceptionHandler(Exception.class)
  public ApiResp exception(HttpServletRequest request, Exception e) {
    log.error(
      "method: exception, endPoint: {} , queryString: {}",
      request.getRequestURI(),
      request.getQueryString(),
      e);

    return new ApiResp()
      .setCode(ERROR_UNKNOWN.getUniversalCode())
      .setMsg(messageSource.getMessage(
        ERROR_UNKNOWN.getValue(),
        null,
        LocaleContextHolder.getLocale()));
  }
}
