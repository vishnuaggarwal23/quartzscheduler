package com.vishnu.aggarwal.rest.service;

import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.BooleanUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

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

    private Trigger trigger;

    public void createNewUnscheduledApiJob(QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException {
        JobKey jobKey = new JobKey(quartzDTO.getJob().getKeyName(), quartzDTO.getJob().getGroupName());
        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(quartzDTO));
        JobBuilder jobBuilder = newJob((Class<? extends Job>) getExecutorClass(quartzDTO.getExecutorClass()))
                .requestRecovery(quartzDTO.getJob().getRecover())
                .storeDurably(quartzDTO.getJob().getDurability())
                .withIdentity(jobKey)
                .withDescription(quartzDTO.getJob().getDescription())
                .setJobData(jobDataMap);
        JobDetail jobDetail = jobBuilder.build();

        if (BooleanUtils.isTrue(quartzDTO.getJob().getDurability())) {
            quartzScheduler.addJob(jobDetail, false);
        } else {
            quartzScheduler.addJob(jobDetail, false, true);
        }
    }

    public void createNewScheduledApiSimpleJob(QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException {
        JobKey jobKey = new JobKey(quartzDTO.getJob().getKeyName(), quartzDTO.getJob().getGroupName());
        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(quartzDTO));
        JobBuilder jobBuilder = newJob((Class<? extends Job>) getExecutorClass(quartzDTO.getExecutorClass()))
                .withIdentity(jobKey)
                .requestRecovery(quartzDTO.getJob().getRecover())
                .withDescription(quartzDTO.getJob().getDescription())
                .storeDurably(quartzDTO.getJob().getDurability())
                .setJobData(jobDataMap);
        JobDetail jobDetail = jobBuilder.build();
        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDataMap)
                .withDescription(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getTriggerDescription())
                .withIdentity(new TriggerKey(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getKeyName(), quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getGroupName()));
        if(BooleanUtils.isTrue(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartNow())){
            triggerBuilder = triggerBuilder.startNow();
        } else {
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartTime());
        }

        switch (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatType()) {
            case REPEAT_BY_SECOND:
                if (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(simpleSchedule().withIntervalInSeconds(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()).repeatForever());
                }
                break;
            case REPEAT_BY_MINUTE:
                if (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(simpleSchedule().withIntervalInMinutes(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()).repeatForever());
                }
                break;
            case REPEAT_BY_HOUR:
                if (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(simpleSchedule().withIntervalInHours(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()).repeatForever());
                }
                break;
        }
        quartzScheduler.scheduleJob(jobDetail, triggerBuilder.build());
    }

    public void createNewScheduledApiCronJob(QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException {
        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(quartzDTO));
        JobBuilder jobBuilder = newJob((Class<? extends Job>) getExecutorClass(quartzDTO.getExecutorClass()))
                .withIdentity(new JobKey(quartzDTO.getJob().getKeyName(), quartzDTO.getJob().getGroupName()))
                .requestRecovery(quartzDTO.getJob().getRecover())
                .withDescription(quartzDTO.getJob().getDescription())
                .storeDurably(quartzDTO.getJob().getDurability())
                .setJobData(jobDataMap);
        JobDetail jobDetail = jobBuilder.build();
        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDataMap)
                .withDescription(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getTriggerDescription())
                .withIdentity(new TriggerKey(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getKeyName(), quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getGroupName()))
                .withSchedule(cronSchedule(quartzDTO.getApiJobData().getCronJobScheduler().getCronExpression()));
        if(BooleanUtils.isTrue(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartNow())){
            triggerBuilder = triggerBuilder.startNow();
        } else {
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartTime());
        }

        quartzScheduler.scheduleJob(jobDetail, triggerBuilder.build());
    }

    public void createNewSimpleTriggerForJob(QuartzDTO quartzDTO) throws SchedulerException {
        JobKey jobKey = new JobKey(quartzDTO.getJob().getKeyName(), quartzDTO.getJob().getGroupName());
        JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getTriggerDescription())
                .withIdentity(new TriggerKey(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getKeyName(), quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getGroupName()));
        if(BooleanUtils.isTrue(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartNow())){
            triggerBuilder = triggerBuilder.startNow();
        } else {
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartTime());
        }

        switch (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatType()) {
            case REPEAT_BY_SECOND:
                if (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(simpleSchedule().withIntervalInSeconds(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()).repeatForever());
                }
                break;
            case REPEAT_BY_MINUTE:
                if (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(simpleSchedule().withIntervalInMinutes(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()).repeatForever());
                }
                break;
            case REPEAT_BY_HOUR:
                if (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(simpleSchedule().withIntervalInHours(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()).repeatForever());
                }
                break;
        }
        quartzScheduler.scheduleJob(triggerBuilder.build());
    }

    public void createNewCronTriggerForJob(QuartzDTO quartzDTO) throws SchedulerException {
        JobKey jobKey = new JobKey(quartzDTO.getJob().getKeyName(), quartzDTO.getJob().getGroupName());
        JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getTriggerDescription())
                .withIdentity(new TriggerKey(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getKeyName(), quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getGroupName()))
                .withSchedule(cronSchedule(quartzDTO.getApiJobData().getCronJobScheduler().getCronExpression()));
        if(BooleanUtils.isTrue(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartNow())){
            triggerBuilder = triggerBuilder.startNow();
        } else {
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartTime());
        }

        quartzScheduler.scheduleJob(triggerBuilder.build());
    }

    private Map<String, String> createJobDataMap(QuartzDTO quartzDTO) {
        Map<String, String> jobData = new HashMap<String, String>();
        if (Objects.nonNull(quartzDTO) && Objects.nonNull(quartzDTO.getApiJobData())) {
            if (!CollectionUtils.isEmpty(quartzDTO.getApiJobData().getRequestHeaders())) {
                quartzDTO.getApiJobData().getRequestHeaders().stream().filter(Objects::nonNull).forEach(it -> {
                    jobData.put("header_" + it.getKey(), it.getValue());
                });
            }
            jobData.put("request-url", quartzDTO.getApiJobData().getRequestUrl());
        }
        return jobData;
    }

    private Class<?> getExecutorClass(JobExecutorClass jobExecutorClass) throws ClassNotFoundException {
        return Class.forName(jobExecutorClass.getPackageName());
    }
}
