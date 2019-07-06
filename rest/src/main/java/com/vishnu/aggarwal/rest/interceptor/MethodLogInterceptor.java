package com.vishnu.aggarwal.rest.interceptor;

/*
Created by vishnu on 6/7/19 4:23 PM
*/

import lombok.extern.apachecommons.CommonsLog;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.springframework.aop.interceptor.DebugInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * The type Method log interceptor.
 */
@Component
@CommonsLog
public class MethodLogInterceptor extends DebugInterceptor {
    /**
     * Instantiates a new Method log interceptor.
     */
    public MethodLogInterceptor() {
        super();
    }

    @Override
    protected Object invokeUnderTrace(MethodInvocation invocation, Log logger) throws Throwable {
        String invocationDescription = getInvocationDescription(invocation);
        String name = invocation.toString();
        StopWatch stopWatch = new StopWatch(name);
        writeToLog(logger, "Entering " + invocationDescription);
        try {
            stopWatch.start();
            Object rval = invocation.proceed();
            writeToLog(logger, "Exiting " + invocationDescription);
            return rval;
        } catch (Throwable ex) {
            writeToLog(logger, "Exception thrown in " + invocationDescription, ex);
            throw ex;
        } finally {
            stopWatch.stop();
            writeToLog(logger, stopWatch.shortSummary());
        }
    }
}
