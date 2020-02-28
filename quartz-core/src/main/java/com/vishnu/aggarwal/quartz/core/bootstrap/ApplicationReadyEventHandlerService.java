package com.vishnu.aggarwal.quartz.core.bootstrap;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

/**
 * The type Application ready event handler service.
 */
@CommonsLog
@Getter
public abstract class ApplicationReadyEventHandlerService implements ApplicationListener<ApplicationReadyEvent> {
    /**
     * The Bootstrap enabled.
     */
    @Value("${bootstrap.enabled:false}")
    Boolean bootstrapEnabled;
}
