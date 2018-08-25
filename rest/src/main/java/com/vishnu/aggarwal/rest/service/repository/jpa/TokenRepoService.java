package com.vishnu.aggarwal.rest.service.repository.jpa;

/*
Created by vishnu on 21/4/18 2:36 PM
*/

import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.repository.jpa.TokenRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

import static com.vishnu.aggarwal.core.enums.Status.ACTIVE;
import static com.vishnu.aggarwal.core.util.DateUtils.getStart;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.System.currentTimeMillis;

/**
 * The type Token repo service.
 */
@Service
@CommonsLog
public class TokenRepoService extends BaseRepoService<Token, Long> {

    /**
     * The Token repository.
     */
    private final TokenRepository tokenRepository;

    /**
     * Instantiates a new Token repo service.
     *
     * @param tokenRepository the token repository
     */
    @Autowired
    public TokenRepoService(TokenRepository tokenRepository) {
        this.tokenRepository = tokenRepository;
    }

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

    /**
     * Find by token and is deleted token.
     *
     * @param token     the token
     * @param isDeleted the is deleted
     * @return the token
     */
    public Token findByTokenAndIsDeleted(String token, Boolean isDeleted) {
        return tokenRepository.findByTokenAndIsDeleted(token, isDeleted);
    }

    /**
     * Find all expired tokens list.
     *
     * @return the list
     */
    public List<Token> findAllExpiredTokens() {
        CriteriaQuery<Token> criteriaQuery = getBaseCriteriaSelectImpl();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        Root<Token> tokenRoot = getRoot(criteriaQuery);
        criteriaQuery
                .where(
                        criteriaBuilder.equal(tokenRoot.get("status"), ACTIVE),
                        criteriaBuilder.isNotNull(tokenRoot.get("expirationDate")),
                        criteriaBuilder.lessThan(tokenRoot.get("expirationDate"), getStart(new Date(currentTimeMillis())))
                );
        return (List<Token>) selectQuery(criteriaQuery, TRUE, FALSE, null);
    }
}
