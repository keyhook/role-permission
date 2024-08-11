package org.ovida.auth.domain.repo;

import java.util.Collection;
import java.util.Set;
import org.ovida.auth.domain.model.RolePrivilege;

public interface RolePrivilegeRepo {

  void insert(Collection<RolePrivilege> rolePrivileges);

  void deleteByRoleName(String roleName);

  Set<RolePrivilege> findByRoleName(String roleName);
}
