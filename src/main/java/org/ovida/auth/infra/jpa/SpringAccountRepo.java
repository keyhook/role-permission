package org.ovida.auth.infra.jpa;

import java.util.Optional;
import java.util.UUID;
import org.ovida.auth.domain.model.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringAccountRepo extends JpaRepository<Account, UUID> {

  Optional<Account> findByEmail(String email);

  Page<Account> findByOrderByUpdatedAtDesc(Pageable pageable);
}
