package org.ovida.auth.infra.jpa;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ovida.auth.domain.exception.DomainCode;
import org.ovida.auth.domain.exception.DomainException;
import org.ovida.auth.domain.model.Account;
import org.ovida.auth.domain.repo.AccountRepo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Repository
public class JpaAccountRepo implements AccountRepo {

  private final SpringAccountRepo springAccountRepo;

  @Override
  public Account requireById(UUID id) {
    var account = springAccountRepo
      .findById(id)
      .orElseThrow(() -> new DomainException(DomainCode.ENTITY_NOT_FOUND, Map.of(
        "entity", Account.class,
        "id", id)));

    log.info("method: requireById - id: {} - account: {}", id, account);

    return account;
  }

  @Override
  public Page<Account> findAll(int page, int pageSize) {
    var accounts = springAccountRepo.findByOrderByUpdatedAtDesc(Pageable.ofSize(pageSize).withPage(page));

    log.info("method: findAll - page: {} - pageSize: {}", page, pageSize);

    return accounts;
  }

  @Override
  public Optional<Account> findByEmail(String email) {
    var account = springAccountRepo.findByEmail(email);

    log.info("method: findByEmail, email: {} , account: {}", email, account);

    return account;
  }
}
