package com.vishnu.aggarwal.rest.config;

/*
Created by vishnu on 6/7/19 3:09 PM
*/

import com.vishnu.aggarwal.rest.interceptor.MethodLogInterceptor;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.aop.Advisor;
import org.springframework.aop.aspectj.AspectJExpressionPointcut;
import org.springframework.aop.support.DefaultPointcutAdvisor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy(proxyTargetClass = true)
@Aspect
public class AopConfiguration {

    private final MethodLogInterceptor methodLogInterceptor;

    public AopConfiguration(MethodLogInterceptor methodLogInterceptor) {
        this.methodLogInterceptor = methodLogInterceptor;
    }

    @Pointcut("execution(* com.vishnu.aggarwal.*.service..*.*(..))")
    public void service() {
    }

    @Pointcut("execution(* com.vishnu.aggarwal.*.controller..*.*(..))")
    public void controller() {
    }

    /**
     * Filters.
     */
//    @Pointcut("execution(* com.vishnu.aggarwal.*.filters..*.*(..))")
//    public void filters() {
//    }

    @Pointcut("execution(* com.vishnu.aggarwal.*.util..*.*(..))")
    public void util() {
    }

    @Pointcut("execution(* com.vishnu.aggarwal.*.jobs..*.*(..))")
    public void jobs() {
    }

    @Pointcut("execution(* com.vishnu.aggarwal.*.bootstrap..*.*(..))")
    public void bootstrap() {
    }

    @Pointcut("service() || controller() || util() || jobs() || bootstrap()")
    public void monitor() {
    }

    @Bean
    public Advisor performanceMonitorAdvisor() {
        AspectJExpressionPointcut pointcut = new AspectJExpressionPointcut();
        pointcut.setExpression(String.format("%s.AopConfiguration.monitor()", this.getClass().getPackage().getName()));
        return new DefaultPointcutAdvisor(pointcut, methodLogInterceptor);
    }
}
