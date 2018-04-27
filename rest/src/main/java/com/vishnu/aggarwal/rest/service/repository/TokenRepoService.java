package com.vishnu.aggarwal.rest.service.repository;

/*
Created by vishnu on 21/4/18 2:36 PM
*/

import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.repository.TokenRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

import static com.vishnu.aggarwal.core.enums.Status.ACTIVE;
import static com.vishnu.aggarwal.core.util.DateUtils.getStart;
import static java.lang.Boolean.TRUE;
import static java.lang.System.currentTimeMillis;
import static org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY;
import static org.hibernate.criterion.Restrictions.*;

@Service
@CommonsLog
public class TokenRepoService extends BaseRepoService<Token, Long> {

    @Autowired
    TokenRepository tokenRepository;

    @Override
    protected Class<Token> getEntityClass() {
        return Token.class;
    }

    @Override
    protected JpaRepository<Token, Long> getJpaRepository() {
        return tokenRepository;
    }

    @SuppressWarnings("unchecked")
    @Transactional
    public Token save(Token token) {
        return super.save(token);
    }

    public Token findByTokenAndIsDeleted(String token, Boolean isDeleted){
        return tokenRepository.findByTokenAndIsDeleted(token, isDeleted);
    }

    public List<Token> findAllExpiredTokens() {
        return (List<Token>) getBaseCriteriaImpl()
                .setReadOnly(TRUE)
                .add(eq("status", ACTIVE))
                .add(isNotNull("expirationDate"))
                .add(lt("expirationDate", getStart(new Date(currentTimeMillis()))))
                .setResultTransformer(DISTINCT_ROOT_ENTITY)
                .list();
    }
}
