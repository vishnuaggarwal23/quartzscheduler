package com.vishnu.aggarwal.core.bootstrap;

import lombok.extern.apachecommons.CommonsLog;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;

/**
 * The type Application ready event handler service.
 */
@CommonsLog
public abstract class ApplicationReadyEventHandlerService implements ApplicationListener<ApplicationEvent> {
}
