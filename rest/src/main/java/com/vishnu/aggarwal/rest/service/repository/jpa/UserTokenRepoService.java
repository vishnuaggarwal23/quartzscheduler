package com.vishnu.aggarwal.rest.service.repository.jpa;

import com.vishnu.aggarwal.core.enums.Status;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserToken;
import com.vishnu.aggarwal.rest.repository.jpa.UserTokenRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.List;

import static com.vishnu.aggarwal.core.enums.Status.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;

/*
Created by vishnu on 20/4/18 12:20 PM
*/

@Service
@CommonsLog
@Transactional
public class UserTokenRepoService extends BaseRepoService<UserToken, Long> {

    private final UserTokenRepository userTokenRepository;

    @Autowired
    public UserTokenRepoService(UserTokenRepository userTokenRepository) {
        this.userTokenRepository = userTokenRepository;
    }

    @Override
    protected Class<UserToken> getEntityClass() {
        return UserToken.class;
    }

    @Override
    protected JpaRepository<UserToken, Long> getJpaRepository() {
        return userTokenRepository;
    }

    @CacheEvict(value = {"findUserTokenByToken", "findAllUserTokens", "findUserTokenByUserAndStatus"}, allEntries = true, beforeInvocation = true)
    @SuppressWarnings("unchecked")
    public UserToken save(UserToken userToken) {
        return super.save(userToken);
    }

    @Cacheable(value = "findUserTokenByToken", key = "#xAuthToken", unless = "#result == null")
    @SuppressWarnings("unchecked")
    public UserToken findByToken(String xAuthToken) {
        CriteriaQuery<UserToken> criteriaQuery = getBaseCriteriaSelectImpl();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        Root<UserToken> userToken = getRoot(criteriaQuery);
        Join<UserToken, Token> token = userToken.join("token", JoinType.LEFT);
        criteriaQuery
                .select(userToken)
                .where(
                        criteriaBuilder.equal(userToken.get("status"), ACTIVE),
                        criteriaBuilder.isFalse(userToken.get("isDeleted")),
                        criteriaBuilder.isFalse(token.get("isDeleted")),
                        criteriaBuilder.like(criteriaBuilder.lower(token.get("token")), xAuthToken.toLowerCase())
                );
        return (UserToken) selectQuery(criteriaQuery, TRUE, TRUE, null);
    }

    @CacheEvict(value = {"findUserTokenByToken", "findAllUserTokens", "findUserTokenByUserAndStatus"}, allEntries = true, beforeInvocation = true)
    public Boolean inactivatePreviousUserTokens(User user) {
        CriteriaUpdate<UserToken> criteriaUpdate = getBaseCriteriaUpdateImpl();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        Root<UserToken> root = getRoot(criteriaUpdate);
        criteriaUpdate
                .set("status", EXPIRED)
                .where(criteriaBuilder.not(root.get("status").in(asList(EXPIRED, PASSIVE))))
                .where(criteriaBuilder.equal(root.get("status"), ACTIVE))
                .where(criteriaBuilder.equal(root.get("user"), user));
        return updateQuery(criteriaUpdate) > 0;
    }

    @CacheEvict(value = {"findUserTokenByToken", "findAllUserTokens", "findUserTokenByUserAndStatus"}, allEntries = true, beforeInvocation = true)
    public Boolean inactivateExpiredUserTokens(List<Token> tokens) {
        CriteriaUpdate<UserToken> criteriaUpdate = getBaseCriteriaUpdateImpl();
        Root<UserToken> root = getRoot(criteriaUpdate);
        criteriaUpdate
                .set("status", EXPIRED)
                .where(getCriteriaBuilder().in(root.get("tokens").in(tokens)));
        return updateQuery(criteriaUpdate) > 0;
    }

    @Cacheable(value = "findAllUserTokens", key = "#user.toString()", unless = "#result == null")
    @SuppressWarnings("unchecked")
    public List<UserToken> findAllUserTokens(User user) throws NoResultException {
        CriteriaQuery<UserToken> criteriaQuery = getBaseCriteriaSelectImpl();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        Root<UserToken> userToken = getRoot(criteriaQuery);
        criteriaQuery
                .where(
                        criteriaBuilder.not(userToken.get("status").in(EXPIRED, PASSIVE)),
                        criteriaBuilder.isFalse(userToken.get("isDeleted")),
                        criteriaBuilder.equal(userToken.get("user"), user)
                );
        return (List<UserToken>) selectQuery(criteriaQuery, TRUE, FALSE, null);
    }

    @Cacheable(value = "findUserTokenByUserAndStatus", key = "#user.toString() + #status.toString()", unless = "#result == null")
    @SuppressWarnings("unchecked")
    public UserToken findByUserAndStatus(User user, Status status) throws NoResultException {
        CriteriaQuery<UserToken> criteriaQuery = getBaseCriteriaSelectImpl();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        Root<UserToken> userToken = getRoot(criteriaQuery);
        criteriaQuery
                .where(
                        criteriaBuilder.equal(userToken.get("status"), status),
                        criteriaBuilder.isFalse(userToken.get("isDeleted")),
                        criteriaBuilder.equal(userToken.get("user"), user)
                );
        return (UserToken) selectQuery(criteriaQuery, TRUE, TRUE, null);
    }
}
