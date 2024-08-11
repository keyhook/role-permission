package org.ovida.auth.app.token;


import java.time.LocalDateTime;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ovida.auth.app.component.TokenCryptoEngine;
import org.ovida.auth.domain.exception.DomainCode;
import org.ovida.auth.domain.exception.DomainException;
import org.ovida.auth.domain.model.Account;
import org.ovida.auth.domain.model.Token;
import org.ovida.auth.domain.repo.AccountRepo;
import org.ovida.auth.domain.repo.RolePrivilegeRepo;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class StdTokenAppSvc implements TokenAppSvc {

  private final AccountRepo accountRepo;

  private final RolePrivilegeRepo rolePrivilegeRepo;

  private final TokenCryptoEngine tokenCryptoEngine;

  private final TokenConfig tokenConfig;

  @Override
  public String create(CreateTokenCmd cmd) {
    log.info("method: create - cmd: {}", cmd);

    var account = accountRepo
      .findByEmail(cmd.getEmail())
      .filter(Account::active)
      .orElseThrow(() -> new DomainException(DomainCode.CREDENTIALS_INVALID, Map.of("email", cmd.getEmail())));

    // WARNING: for demonstration purposes only, we must not store passwords in plain text
    if (!cmd.getPassword().equals(account.getPassword())) {
      throw new DomainException(DomainCode.CREDENTIALS_INVALID, Map.of("email", cmd.getEmail()));
    }

    var privilegeNames = rolePrivilegeRepo
      .findByRoleName(account.getRoleName())
      .stream()
      .map(relation -> relation.getRolePrivilegeId().getPrivilegeName())
      .collect(Collectors.toSet());

    var token = new Token()
      .setId(UUID.randomUUID())
      .setAccountId(account.getId())
      .setEmail(account.getEmail())
      .setRoleName(account.getRoleName())
      .setPrivilegeNames(privilegeNames)
      .setExpiredAt(LocalDateTime.now().plus(tokenConfig.getExpiration()));

    var encodedToken = tokenCryptoEngine.sign(token);

    log.info("method: create");

    return encodedToken;
  }
}
