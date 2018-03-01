package com.vishnu.aggarwal.rest.config;

import org.quartz.Scheduler;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.io.IOException;
import java.util.Properties;

/**
 * The type Quartz beans.
 */
@Configuration(value = "quartzBeans")
@EnableTransactionManagement
@EnableJpaRepositories({"com.vishnu.aggarwal.rest.entity"})
public class QuartzBeans {

    /**
     * Job factory job factory.
     *
     * @param applicationContext the application context
     * @return the job factory
     */
    @Bean(value = "jobFactory")
    public JobFactory jobFactory(ApplicationContext applicationContext) {
        AutowiringSpringBeanJobFactory autowiringSpringBeanJobFactory = new AutowiringSpringBeanJobFactory();
        autowiringSpringBeanJobFactory.setApplicationContext(applicationContext);
        return autowiringSpringBeanJobFactory;
    }

    /**
     * Quartz properties properties.
     *
     * @return the properties
     * @throws IOException the io exception
     */
    @Bean(value = "quartzProperties")
    public Properties quartzProperties() throws IOException {
        PropertiesFactoryBean propertiesFactoryBean = new PropertiesFactoryBean();
        propertiesFactoryBean.setLocation(new ClassPathResource("/quartz.yml"));
        propertiesFactoryBean.afterPropertiesSet();
        return propertiesFactoryBean.getObject();
    }

    /**
     * Scheduler factory bean scheduler factory bean.
     *
     * @param dataSource the data source
     * @param jobFactory the job factory
     * @return the scheduler factory bean
     * @throws IOException the io exception
     */
    @Bean(value = "schedulerFactoryBean")
    public SchedulerFactoryBean schedulerFactoryBean(DataSource dataSource, JobFactory jobFactory) throws IOException {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        schedulerFactoryBean.setOverwriteExistingJobs(true);
        schedulerFactoryBean.setDataSource(dataSource);
        schedulerFactoryBean.setJobFactory(jobFactory);
        schedulerFactoryBean.setQuartzProperties(quartzProperties());
        return schedulerFactoryBean;
    }

    /**
     * Quartz scheduler scheduler.
     *
     * @param schedulerFactoryBean the scheduler factory bean
     * @return the scheduler
     */
    @Bean(value = "quartzScheduler")
    public Scheduler quartzScheduler(SchedulerFactoryBean schedulerFactoryBean) {
        return schedulerFactoryBean.getScheduler();
    }
}
