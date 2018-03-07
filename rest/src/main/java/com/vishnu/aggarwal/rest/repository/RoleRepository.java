package com.vishnu.aggarwal.rest.repository;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

import com.vishnu.aggarwal.rest.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByAuthorityAndIsDeleted(String authority, Boolean isDeleted);

    Integer countByIsDeleted(Boolean isDeleted);
}
