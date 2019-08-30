package com.vishnu.aggarwal.rest.repository.jpa;

import com.vishnu.aggarwal.rest.entity.Authority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

@Repository
@Transactional
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
    Authority findByNameAndIsDeleted(String name, Boolean isDeleted);
}
