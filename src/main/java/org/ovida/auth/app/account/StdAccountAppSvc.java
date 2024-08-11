package org.ovida.auth.app.account;

import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ovida.auth.app.search.SearchQuery;
import org.ovida.auth.app.search.SearchResult;
import org.ovida.auth.domain.model.Account;
import org.ovida.auth.domain.repo.AccountRepo;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class StdAccountAppSvc implements AccountAppSvc {

  private final AccountRepo accountRepo;

  @Override
  public SearchResult search(SearchQuery searchQuery) {
    log.info("method: search - searchQuery: {}", searchQuery);

    var accounts = accountRepo.findAll(searchQuery.getPage(), searchQuery.getPageSize());

    var searchResult = new SearchResult()
      .setRecords(accounts.getContent())
      .setPage(searchQuery.getPage())
      .setPageSize(searchQuery.getPageSize())
      .setTotal(accounts.getTotalElements());

    log.info("method: search - searchResult: {}", searchResult);

    return searchResult;
  }

  @Override
  public Account get(UUID accountId) {
    log.info("method: get - accountId: {}", accountId);

    var account = accountRepo.requireById(accountId);

    log.info("method: get - account: {}", account);

    return account;
  }
}
