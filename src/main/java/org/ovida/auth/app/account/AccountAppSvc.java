package org.ovida.auth.app.account;

import java.util.UUID;
import org.ovida.auth.app.search.SearchQuery;
import org.ovida.auth.app.search.SearchResult;
import org.ovida.auth.domain.model.Account;

public interface AccountAppSvc {

  SearchResult search(SearchQuery searchQuery);

  Account get(UUID accountId);
}
