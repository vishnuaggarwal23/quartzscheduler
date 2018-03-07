package com.vishnu.aggarwal.rest.service.repository;

/*
Created by vishnu on 6/3/18 10:39 AM
*/

import com.vishnu.aggarwal.rest.entity.Role;
import com.vishnu.aggarwal.rest.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import static java.lang.Boolean.FALSE;

@Service
public class RoleRepoService extends BaseRepoService<Role, Long> {

    @Autowired
    RoleRepository roleRepository;

    @Override
    protected Class<Role> getEntityClass() {
        return Role.class;
    }

    @Override
    protected JpaRepository<Role, Long> getJpaRepository() {
        return roleRepository;
    }

    public Role findByAuthority(String authority) {
        return roleRepository.findByAuthorityAndIsDeleted(authority, FALSE);
    }

    @SuppressWarnings("unchecked")
    public Role save(Role role) {
        return super.save(role);
    }

    public Integer countNonDeletedRoles() {
        return roleRepository.countByIsDeleted(FALSE);
    }
}
