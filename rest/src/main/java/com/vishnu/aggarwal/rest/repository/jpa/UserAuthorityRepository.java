package com.vishnu.aggarwal.rest.repository.jpa;

import com.vishnu.aggarwal.rest.entity.Authority;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserAuthority;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

@Repository
@Transactional
public interface UserAuthorityRepository extends JpaRepository<UserAuthority, Long> {
    UserAuthority findByUserAndAuthorityAndIsDeleted(User user, Authority authority, Boolean isDeleted);
}
