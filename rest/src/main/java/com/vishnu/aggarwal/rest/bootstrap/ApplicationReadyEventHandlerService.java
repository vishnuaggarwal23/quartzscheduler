package com.vishnu.aggarwal.rest.bootstrap;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.stereotype.Component;

@Component
@CommonsLog
public class ApplicationReadyEventHandlerService extends com.vishnu.aggarwal.core.bootstrap.ApplicationReadyEventHandlerService {
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        log.info("*****************Application Ready Event Handler called for Rest Application.");
    }
}
