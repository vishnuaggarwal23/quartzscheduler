package com.vishnu.aggarwal.admin.bootstrap;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

/**
 * The type Application ready event handler service.
 */
@Component
@CommonsLog
public class ApplicationReadyEventHandlerService extends com.vishnu.aggarwal.core.bootstrap.ApplicationReadyEventHandlerService {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        if (getBootstrapEnabled()) {
            log.info("******************** Application Ready Event Handler called for admin.");
        }
    }
}
