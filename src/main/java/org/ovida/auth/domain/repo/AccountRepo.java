package org.ovida.auth.domain.repo;

import java.util.Optional;
import java.util.UUID;
import org.ovida.auth.domain.model.Account;
import org.springframework.data.domain.Page;

public interface AccountRepo {

  Account requireById(UUID id);

  Optional<Account> findByEmail(String email);

  Page<Account> findAll(int page, int pageSize);
}
