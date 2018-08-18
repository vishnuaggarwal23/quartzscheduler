package com.vishnu.aggarwal.rest.jobs;

/*
Created by vishnu on 24/4/18 12:03 PM
*/

import com.vishnu.aggarwal.rest.entity.Token;
import com.vishnu.aggarwal.rest.service.repository.jpa.TokenRepoService;
import com.vishnu.aggarwal.rest.service.repository.jpa.UserTokenRepoService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.lang.System.out;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * The type Jobs.
 */
@Component
@CommonsLog
public class Jobs {

    /**
     * The Token repo service.
     */
    @Autowired
    TokenRepoService tokenRepoService;

    /**
     * The User token repo service.
     */
    @Autowired
    UserTokenRepoService userTokenRepoService;

    /**
     * Inactivate expired user tokens.
     */
    @Scheduled(zone = "UTC", cron = "0 0 0 * * ?")
    public void inactivateExpiredUserTokens() {
        List<Token> tokens = tokenRepoService.findAllExpiredTokens();
        if (!isEmpty(tokens)) {
            log.info("*********** Found expired tokens to inactivate");
            tokens.forEach((Token it) -> {
                out.println(it.toString());
            });
            log.info("**********");
            if (userTokenRepoService.inactivateExpiredUserTokens(tokens)) {
                log.info("*********** Expired tokens inactivated *************");
            }
        }
    }
}