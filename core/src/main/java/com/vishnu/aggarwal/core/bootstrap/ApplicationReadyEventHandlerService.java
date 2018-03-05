package com.vishnu.aggarwal.core.bootstrap;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * The type Application ready event handler service.
 */
@CommonsLog
@Getter
public abstract class ApplicationReadyEventHandlerService implements ApplicationListener<ApplicationEvent> {
    /**
     * The Bootstrap enabled.
     */
    @Value("${bootstrap.enabled:false}")
    Boolean bootstrapEnabled;
}
