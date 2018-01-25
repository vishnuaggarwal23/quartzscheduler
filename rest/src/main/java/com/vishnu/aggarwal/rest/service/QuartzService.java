package com.vishnu.aggarwal.rest.service;

import com.vishnu.aggarwal.core.dto.JobDTO;
import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.BooleanUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

@Service
@CommonsLog
public class QuartzService {

    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    @Autowired
    Scheduler quartzScheduler;

    CronExpression cronExpression;

    private Trigger trigger;

    public void createUnscheduledApiJob(JobDTO jobDTO) throws ClassNotFoundException, SchedulerException {
        JobKey jobKey = new JobKey(jobDTO.getKeyName(), jobDTO.getGroupName());
        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(jobDTO));
        JobBuilder jobBuilder = newJob((Class<? extends Job>) getExecutorClass(jobDTO.getExecutorClass()))
                .requestRecovery(jobDTO.getShouldRecover())
                .storeDurably(jobDTO.getDurability())
                .withIdentity(jobKey)
                .withDescription(jobDTO.getDescription())
                .setJobData(jobDataMap);
        JobDetail jobDetail = jobBuilder.build();

        if (BooleanUtils.isTrue(jobDTO.getDurability())) {
            quartzScheduler.addJob(jobDetail, false);
        } else {
            quartzScheduler.addJob(jobDetail, false, true);
        }
    }

    public void createScheduledApiSimpleJob(JobDTO jobDTO) throws ClassNotFoundException, SchedulerException {
        JobKey jobKey = new JobKey(jobDTO.getKeyName(), jobDTO.getGroupName());
        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(jobDTO));
        JobBuilder jobBuilder = newJob((Class<? extends Job>) getExecutorClass(jobDTO.getExecutorClass()))
                .withIdentity(jobKey)
                .requestRecovery(jobDTO.getShouldRecover())
                .withDescription(jobDTO.getDescription())
                .storeDurably(jobDTO.getDurability())
                .setJobData(jobDataMap);
        JobDetail jobDetail = jobBuilder.build();
        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDataMap)
                .withDescription(jobDTO.getSimpleJobScheduler().getTriggerDescription())
                .withIdentity(new TriggerKey(jobDTO.getSimpleJobScheduler().getTriggerKeyName(), jobDTO.getSimpleJobScheduler().getTriggerGroupName()))
                .startAt(jobDTO.getSimpleJobScheduler().getStartTime());

        switch (jobDTO.getSimpleJobScheduler().getRepeatType()) {
            case REPEAT_BY_SECOND:
                if (jobDTO.getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(simpleSchedule().withIntervalInSeconds(jobDTO.getSimpleJobScheduler().getRepeatInterval().getRepeatValue()).repeatForever());
                }
                break;
            case REPEAT_BY_MINUTE:
                if (jobDTO.getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(simpleSchedule().withIntervalInMinutes(jobDTO.getSimpleJobScheduler().getRepeatInterval().getRepeatValue()).repeatForever());
                }
                break;
            case REPEAT_BY_HOUR:
                if (jobDTO.getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(simpleSchedule().withIntervalInHours(jobDTO.getSimpleJobScheduler().getRepeatInterval().getRepeatValue()).repeatForever());
                }
                break;
        }
        quartzScheduler.scheduleJob(jobDetail, triggerBuilder.build());
    }

    public void createScheduledApiCronJob(JobDTO jobDTO) throws ClassNotFoundException, SchedulerException {
        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(jobDTO));
        JobBuilder jobBuilder = newJob((Class<? extends Job>) getExecutorClass(jobDTO.getExecutorClass()))
                .withIdentity(new JobKey(jobDTO.getKeyName(), jobDTO.getGroupName()))
                .requestRecovery(jobDTO.getShouldRecover())
                .withDescription(jobDTO.getDescription())
                .storeDurably(jobDTO.getDurability())
                .setJobData(jobDataMap);
        JobDetail jobDetail = jobBuilder.build();
        quartzScheduler.scheduleJob(jobDetail,
                newTrigger()
                        .forJob(jobDetail)
                        .usingJobData(jobDataMap)
                        .withDescription(jobDTO.getCronJobScheduler().getTriggerDescription())
                        .withIdentity(new TriggerKey(jobDTO.getCronJobScheduler().getTriggerKeyName(), jobDTO.getCronJobScheduler().getTriggerGroupName()))
                        .startNow()
                        .withSchedule(cronSchedule(jobDTO.getCronJobScheduler().getCronExpression()))
                        .build());
    }

    private Map<String, String> createJobDataMap(JobDTO jobDTO) {
        Map<String, String> jobData = new HashMap<String, String>();
        if(Objects.nonNull(jobDTO) && Objects.nonNull(jobDTO.getApiJobData()) && Objects.nonNull(jobDTO.getApiJobData().getRequestHeaders())){
            jobDTO.getApiJobData().getRequestHeaders().stream().filter(Objects::nonNull).forEach(it -> {
                jobData.put("header_" + it.getKey(), it.getValue());
            });
            jobData.put("request-url", jobDTO.getApiJobData().getRequestUrl());
        }
        return jobData;
    }

    private Class<?> getExecutorClass(JobExecutorClass jobExecutorClass) throws ClassNotFoundException {
        return Class.forName(jobExecutorClass.getPackageName());
    }
}
