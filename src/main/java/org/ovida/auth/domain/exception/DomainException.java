package org.ovida.auth.domain.exception;

import java.util.Map;

public class DomainException extends RuntimeException {

  private final DomainCode domainCode;

  private final Map<String, Object> moreInfo;

  public DomainException(DomainCode domainCode, Map<String, Object> moreInfo) {
    super("%s - moreInfo: %s".formatted(domainCode, moreInfo));
    this.domainCode = domainCode;
    this.moreInfo = moreInfo;
  }

  public DomainException(DomainCode domainCode, Map<String, Object> moreInfo, Throwable cause) {
    super("%s - moreInfo: %s".formatted(domainCode, moreInfo), cause);
    this.domainCode = domainCode;
    this.moreInfo = moreInfo;
  }

  public DomainCode getDomainCode() {
    return domainCode;
  }

  public Object[] getArgs() {
    return moreInfo
      .values()
      .toArray();
  }

  public Map<String, Object> getMoreInfo() {
    return moreInfo;
  }
}
