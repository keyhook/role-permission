package org.ovida.auth.app.role;

import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ovida.auth.app.component.DbTxnMgr;
import org.ovida.auth.domain.model.RolePrivilege;
import org.ovida.auth.domain.repo.RolePrivilegeRepo;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
@Slf4j
public class StdRoleAppSvc implements RoleAppSvc {

  private final RolePrivilegeRepo rolePrivilegeRepo;

  private final DbTxnMgr dbTxnMgr;

  @Override
  public void update(UpdateRoleCmd cmd) {
    log.info("method: update - cmd: {}", cmd);

    var rolePrivileges = cmd
        .getPrivilegeNames()
        .stream()
        .map(privilege -> new RolePrivilege(cmd.getRoleName(), privilege))
        .collect(Collectors.toSet());

    dbTxnMgr.doInTx(() -> {
      rolePrivilegeRepo.deleteByRoleName(cmd.getRoleName());
      rolePrivilegeRepo.insert(rolePrivileges);
    });

    log.info("method: update");
  }
}
