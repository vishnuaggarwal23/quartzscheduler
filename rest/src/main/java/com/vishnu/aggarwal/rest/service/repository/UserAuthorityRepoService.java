package com.vishnu.aggarwal.rest.service.repository;

import com.vishnu.aggarwal.rest.entity.Authority;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserAuthority;
import com.vishnu.aggarwal.rest.repository.UserAuthorityRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

/*
Created by vishnu on 20/4/18 2:55 PM
*/

@Service
@CommonsLog
public class UserAuthorityRepoService extends BaseRepoService<UserAuthority, Long> {
    @Autowired
    UserAuthorityRepository userAuthorityRepository;

    @Override
    protected Class<UserAuthority> getEntityClass() {
        return null;
    }

    @Override
    protected JpaRepository<UserAuthority, Long> getJpaRepository() {
        return userAuthorityRepository;
    }

    @SuppressWarnings("unchecked")
    public UserAuthority save(UserAuthority userAuthority) {
        return userAuthorityRepository.save(userAuthority);
    }

    public UserAuthority findByUserAndAuthorityAndIsDeleted(User user, Authority authority, Boolean isDeleted) {
        return userAuthorityRepository.findByUserAndAuthorityAndIsDeleted(user, authority, isDeleted);
    }
}
