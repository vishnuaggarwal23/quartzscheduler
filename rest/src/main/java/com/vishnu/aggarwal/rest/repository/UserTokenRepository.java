package com.vishnu.aggarwal.rest.repository;

import com.vishnu.aggarwal.rest.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

@Repository
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    UserToken findByToken(String token);
}
