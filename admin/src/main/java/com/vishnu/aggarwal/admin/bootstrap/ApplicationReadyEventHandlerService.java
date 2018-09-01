package com.vishnu.aggarwal.admin.bootstrap;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.stereotype.Component;

/**
 * The type Application ready event handler service.
 */
@Component
@CommonsLog
public class ApplicationReadyEventHandlerService extends com.vishnu.aggarwal.core.bootstrap.ApplicationReadyEventHandlerService {
    @Override
    public void onApplicationEvent(ApplicationReadyEvent event) {
        if (getBootstrapEnabled()) {
            log.info("[Application Bootstrap] Application Ready Event Handler called for admin.");
        }
    }
}
