package com.vishnu.aggarwal.rest.service.repository;

import com.vishnu.aggarwal.core.enums.Status;
import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.entity.UserToken;
import com.vishnu.aggarwal.rest.repository.UserTokenRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.criteria.Root;
import java.util.List;

import static com.vishnu.aggarwal.core.enums.Status.*;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.util.Arrays.asList;
import static org.hibernate.criterion.CriteriaSpecification.DISTINCT_ROOT_ENTITY;
import static org.hibernate.criterion.Restrictions.*;

/*
Created by vishnu on 20/4/18 12:20 PM
*/

/**
 * The type User token repo service.
 */
@Service
@CommonsLog
public class UserTokenRepoService extends BaseRepoService<UserToken, Long> {

    /**
     * The User token repository.
     */
    @Autowired
    UserTokenRepository userTokenRepository;

    @Override
    protected Class<UserToken> getEntityClass() {
        return UserToken.class;
    }

    @Override
    protected JpaRepository<UserToken, Long> getJpaRepository() {
        return userTokenRepository;
    }

    public UserToken save(UserToken userToken) {
        return super.save(userToken);
    }

    /**
     * Find by token user token.
     *
     * @param token the token
     * @return the user token
     */
    public UserToken findByToken(String token) {
        return (UserToken) getBaseCriteriaImpl()
                .setReadOnly(TRUE)
                .createAlias("token", "t")
                .add(eq("t.token", token))
                .add(eq("status", ACTIVE))
                .add(eq("isDeleted", FALSE))
                .add(eq("t.isDeleted", FALSE))
                .setResultTransformer(DISTINCT_ROOT_ENTITY)
                .uniqueResult();
    }

    /**
     * Inactivate previous user tokens boolean.
     *
     * @param user the user
     * @return the boolean
     */
    public Boolean inactivatePreviousUserTokens(User user) {
        CriteriaUpdate<UserToken> criteriaUpdate = getBaseCriteriaUpdateImpl();
        Root<UserToken> root = criteriaUpdate.from(getEntityClass());
        criteriaUpdate
                .set("status", EXPIRED)
                .where(getCriteriaBuilder().not(root.get("status").in(asList(EXPIRED, PASSIVE))))
                .where(getCriteriaBuilder().equal(root.get("status"), ACTIVE))
                .where(getCriteriaBuilder().equal(root.get("user"), user));
        return updateQuery(criteriaUpdate) > 0;
    }

    /**
     * Inactivate expired user tokens boolean.
     *
     * @param tokens the tokens
     * @return the boolean
     */
    public Boolean inactivateExpiredUserTokens(List<Token> tokens) {
        CriteriaUpdate<UserToken> criteriaUpdate = getBaseCriteriaUpdateImpl();
        Root<UserToken> root = criteriaUpdate.from(getEntityClass());
        criteriaUpdate
                .set("status", EXPIRED)
                .where(getCriteriaBuilder().in(root.get("tokens").in(tokens)));
        return updateQuery(criteriaUpdate) > 0;
    }

    /**
     * Find all user tokens list.
     *
     * @param user the user
     * @return the list
     */
    public List<UserToken> findAllUserTokens(User user) {
        return (List<UserToken>) getBaseCriteriaImpl().setReadOnly(TRUE)
                .add(eq("user", user))
                .add(not(in("status", asList(EXPIRED, PASSIVE))))
                .add(eq("isDeleted", FALSE))
                .setResultTransformer(DISTINCT_ROOT_ENTITY)
                .list();
    }

    /**
     * Find by user and status user token.
     *
     * @param user   the user
     * @param status the status
     * @return the user token
     */
    public UserToken findByUserAndStatus(User user, Status status) {
        return (UserToken) getBaseCriteriaImpl()
                .setReadOnly(TRUE)
                .add(eq("user", user))
                .add(eq("status", status))
                .add(eq("isDeleted", FALSE))
                .setResultTransformer(DISTINCT_ROOT_ENTITY)
                .uniqueResult();
    }
}
