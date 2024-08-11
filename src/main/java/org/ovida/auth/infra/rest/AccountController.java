package org.ovida.auth.infra.rest;

import java.util.Map;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.ovida.auth.app.account.AccountAppSvc;
import org.ovida.auth.app.search.SearchQuery;
import org.ovida.auth.domain.exception.DomainCode;
import org.ovida.auth.domain.exception.DomainException;
import org.ovida.auth.domain.model.Token;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/accounts")
class AccountController {

  private final AccountAppSvc accountAppSvc;

  @GetMapping("/{accountId}")
  public Object get(
    @AuthenticationPrincipal Token token,
    @PathVariable UUID accountId) {
    if (token.hasReadAccountPrivilege() ||
      token.hasAccountId(accountId)) {
      return accountAppSvc.get(accountId);
    }

    throw new DomainException(DomainCode.FORBIDDEN, Map.of(
      "accountId", token.getAccountId(),
      "email", token.getEmail()));
  }

  @GetMapping
  public Object search(@Validated SearchQuery searchQuery) {
    return accountAppSvc.search(searchQuery);
  }
}
