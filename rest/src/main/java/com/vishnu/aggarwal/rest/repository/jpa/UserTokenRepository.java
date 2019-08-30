package com.vishnu.aggarwal.rest.repository.jpa;

import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.entity.UserToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/*
Created by vishnu on 6/3/18 10:37 AM
*/

@Repository
@Transactional
public interface UserTokenRepository extends JpaRepository<UserToken, Long> {
    UserToken findByToken(Token token);
}
