package org.ovida.auth.infra.jpa;

import jakarta.persistence.EntityManager;
import java.util.Collection;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.ovida.auth.domain.model.RolePrivilege;
import org.ovida.auth.domain.repo.RolePrivilegeRepo;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Repository
@Transactional(readOnly = true)
public class JpaRolePrivilegeRepo implements RolePrivilegeRepo {

  private final EntityManager entityManager;

  private final SpringRolePrivilegeRepo springRolePrivilegeRepo;

  @Override
  @Transactional
  public void insert(Collection<RolePrivilege> rolePrivileges) {
    rolePrivileges.forEach(entityManager::persist);

    log.info("method: insert - rolePrivileges: {}", rolePrivileges);
  }

  @Override
  @Transactional
  public void deleteByRoleName(String roleName) {
    springRolePrivilegeRepo.deleteByRolePrivilegeIdRoleName(roleName);

    log.info("method: deleteByRoleName - roleName: {}", roleName);
  }

  @Override
  public Set<RolePrivilege> findByRoleName(String roleName) {
    var rolePrivileges = springRolePrivilegeRepo.findByRolePrivilegeIdRoleName(roleName);

    log.info("method: findByRoleName - roleName: {} - rolePrivileges: {}", roleName, rolePrivileges);

    return rolePrivileges;
  }
}
