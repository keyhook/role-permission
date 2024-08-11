package org.ovida.auth.infra.jpa;

import java.util.Set;
import org.ovida.auth.domain.model.RolePrivilege;
import org.ovida.auth.domain.model.RolePrivilege.RolePrivilegeId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SpringRolePrivilegeRepo extends JpaRepository<RolePrivilege, RolePrivilegeId> {

  void deleteByRolePrivilegeIdRoleName(String roleName);

  Set<RolePrivilege> findByRolePrivilegeIdRoleName(String roleName);
}
