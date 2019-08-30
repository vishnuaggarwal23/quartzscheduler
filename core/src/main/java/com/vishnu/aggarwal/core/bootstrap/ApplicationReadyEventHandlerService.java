package com.vishnu.aggarwal.core.bootstrap;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;

@CommonsLog
@Getter
public abstract class ApplicationReadyEventHandlerService implements ApplicationListener<ApplicationReadyEvent> {
    @Value("${bootstrap.enabled:false}")
    Boolean bootstrapEnabled;
}
