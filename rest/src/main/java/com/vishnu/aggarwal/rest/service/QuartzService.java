package com.vishnu.aggarwal.rest.service;

import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import com.vishnu.aggarwal.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.vishnu.aggarwal.core.enums.JobExecutorClass.findJobExecutorClassByValue;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Class.forName;
import static java.util.Objects.nonNull;
import static java.util.TimeZone.getDefault;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;
import static org.quartz.impl.matchers.GroupMatcher.triggerGroupEquals;
import static org.springframework.util.Assert.hasText;
import static org.springframework.util.Assert.isTrue;
import static org.springframework.util.Assert.notEmpty;
import static org.springframework.util.Assert.notNull;
import static org.springframework.util.Assert.state;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * The type Quartz service.
 */
@Service
@CommonsLog
public class QuartzService extends BaseService {

    /**
     * The Quartz scheduler.
     */
    private final Scheduler quartzScheduler;

    /**
     * Instantiates a new Quartz service.
     *
     * @param quartzScheduler the quartz scheduler
     */
    @Autowired
    public QuartzService(
            Scheduler quartzScheduler) {
        this.quartzScheduler = quartzScheduler;
    }

    /**
     * Create new unscheduled api job.
     *
     * @param quartzDTO the quartz dto
     * @throws SchedulerException       the scheduler exception
     * @throws NullPointerException     the null pointer exception
     * @throws ClassNotFoundException   the class not found exception
     * @throws IllegalArgumentException the illegal argument exception
     */
    public void createNewUnscheduledApiJob(QuartzDTO quartzDTO) throws SchedulerException, NullPointerException, ClassNotFoundException, IllegalArgumentException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getJob(), formatMessage(getMessage("object.not.set", getMessage("job"))));
        quartzScheduler.addJob(createJobDetail(quartzDTO), false, quartzDTO.getJob().getDurability() ? FALSE : TRUE);
    }

    /**
     * Create new scheduled api simple job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws ClassNotFoundException   the class not found exception
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Date createNewScheduledApiSimpleJob(QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, IllegalArgumentException, NullPointerException, IllegalStateException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notNull(quartzDTO.getApiJobData().getExecutorClass(), formatMessage(getMessage("object.not.set", getMessage("job.executor.class"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("trigger.key.name"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("trigger.group.name"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getDescription(), formatMessage(getMessage("object.not.set", getMessage("trigger.description"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatType(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.type"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.interval"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.value"))));
        state(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue() > 0, formatMessage(getMessage("simple.job.scheduler.repeat.value.greater.than", 0)));

        JobDetail jobDetail = createJobDetail(quartzDTO);
        notNull(jobDetail, formatMessage(getMessage("object.not.set", getMessage("job.detail"))));
        notNull(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));
        notEmpty(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        TriggerKey triggerKey = new TriggerKey(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getKeyName(), quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getGroupName());
        notNull(triggerKey, formatMessage(getMessage("object.not.set", getMessage("trigger.key"))));

        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getDescription())
                .withIdentity(triggerKey);
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        if (isTrue(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartNow())) {
            triggerBuilder = triggerBuilder.startNow();
        } else {
            notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartTime(), formatMessage(getMessage("object.not.set", getMessage("trigger.start.time"))));
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartTime());
        }
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        triggerBuilder = getSimpleTriggerBuilder(quartzDTO, triggerBuilder);
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        return quartzScheduler.scheduleJob(jobDetail, triggerBuilder.build());
    }

    /**
     * Create new scheduled api cron job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws ClassNotFoundException   the class not found exception
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Date createNewScheduledApiCronJob(QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, IllegalArgumentException, NullPointerException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notNull(quartzDTO.getApiJobData().getExecutorClass(), formatMessage(getMessage("object.not.set", getMessage("job.executor.class"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler(), formatMessage(getMessage("object.not.set", getMessage("cron.job.scheduler"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("trigger.key.name"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("trigger.group.name"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getDescription(), formatMessage(getMessage("object.not.set", getMessage("trigger.description"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getCronExpression(), formatMessage(getMessage("object.not.set", getMessage("cron.expression"))));

        JobDetail jobDetail = createJobDetail(quartzDTO);
        notNull(jobDetail, formatMessage(getMessage("object.not.set", getMessage("job.detail"))));
        notNull(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));
        notEmpty(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        TriggerKey triggerKey = new TriggerKey(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getKeyName(), quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getGroupName());
        notNull(triggerKey, formatMessage(getMessage("object.not.set", getMessage("trigger.key"))));

        TriggerBuilder triggerBuilder = getCronTriggerBuilder(quartzDTO, jobDetail, triggerKey);
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        if (isTrue(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartNow())) {
            triggerBuilder = triggerBuilder.startNow();
        } else {
            notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartTime(), formatMessage(getMessage("object.not.set", getMessage("trigger.start.time"))));
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartTime());
        }
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        return quartzScheduler.scheduleJob(jobDetail, triggerBuilder.build());
    }

    /**
     * Create new simple trigger for job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Date createNewSimpleTriggerForJob(QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException, IllegalStateException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getJob(), formatMessage(getMessage("object.not.set", getMessage("job.details"))));
        notNull(quartzDTO.getJob().getDetails(), formatMessage(getMessage("object.not.set", getMessage("job.details"))));
        notNull(quartzDTO.getJob().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("job.key.name"))));
        notNull(quartzDTO.getJob().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notNull(quartzDTO.getApiJobData().getExecutorClass(), formatMessage(getMessage("object.not.set", getMessage("job.executor.class"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("trigger.key.name"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("trigger.group.name"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getDescription(), formatMessage(getMessage("object.not.set", getMessage("trigger.description"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatType(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.type"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.interval"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.value"))));
        state(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue() > 0, formatMessage(getMessage("simple.job.scheduler.repeat.value.greater.than", 0)));

        JobKey jobKey = new JobKey(quartzDTO.getJob().getDetails().getKeyName(), quartzDTO.getJob().getDetails().getGroupName());
        notNull(jobKey, formatMessage(getMessage("object.not.set", getMessage("job.key"))));

        JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        notNull(jobDetail, formatMessage(getMessage("object.not.set", getMessage("job.detail"))));
        notNull(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));
        notEmpty(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        TriggerKey triggerKey = new TriggerKey(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getKeyName(), quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getGroupName());
        notNull(triggerKey, formatMessage(getMessage("object.not.set", getMessage("trigger.key"))));

        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getDescription())
                .withIdentity(triggerKey);
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        if (isTrue(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartNow())) {
            triggerBuilder = triggerBuilder.startNow();
        } else {
            notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartTime(), formatMessage(getMessage("object.not.set", getMessage("trigger.start.time"))));
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartTime());
        }
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        if (nonNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getEndTime())) {
            triggerBuilder = triggerBuilder.endAt(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getEndTime());
        }

        triggerBuilder = getSimpleTriggerBuilder(quartzDTO, triggerBuilder);
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        return quartzScheduler.scheduleJob(triggerBuilder.build());
    }

    /**
     * Create new cron trigger for job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Date createNewCronTriggerForJob(QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getJob(), formatMessage(getMessage("object.not.set", getMessage("job"))));
        notNull(quartzDTO.getJob().getDetails(), formatMessage(getMessage("object.not.set", getMessage("job"))));
        notNull(quartzDTO.getJob().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));
        notNull(quartzDTO.getJob().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("job.key.name"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler(), formatMessage(getMessage("object.not.set", getMessage("cron.job.scheduler"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getCronExpression(), formatMessage(getMessage("object.not.set", getMessage("cron.expression"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("trigger.key.name"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("trigger.group.name"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getDescription(), formatMessage(getMessage("object.not.set", getMessage("trigger.description"))));

        JobKey jobKey = new JobKey(quartzDTO.getJob().getDetails().getKeyName(), quartzDTO.getJob().getDetails().getGroupName());
        notNull(jobKey, formatMessage(getMessage("object.not.set", getMessage("job.key"))));

        JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        notNull(jobDetail, formatMessage(getMessage("object.not.set", getMessage("job.detail"))));
        notNull(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));
        notEmpty(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        TriggerKey triggerKey = new TriggerKey(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getKeyName(), quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getGroupName());
        notNull(triggerKey, formatMessage(getMessage("object.not.set", getMessage("trigger.key"))));

        TriggerBuilder triggerBuilder = getCronTriggerBuilder(quartzDTO, jobDetail, triggerKey);
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        if (isTrue(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartNow())) {
            triggerBuilder = triggerBuilder.startNow();
        } else {
            notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartTime(), formatMessage(getMessage("object.not.set", getMessage("trigger.start.time"))));
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartTime());
        }
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        if (nonNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getEndTime())) {
            triggerBuilder = triggerBuilder.endAt(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getEndTime());
            notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));
        }

        return quartzScheduler.scheduleJob(triggerBuilder.build());
    }

    /**
     * Update existing job.
     *
     * @param quartzDTO the quartz dto
     * @throws ClassNotFoundException   the class not found exception
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public void updateExistingJob(QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, IllegalArgumentException, NullPointerException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getJob(), formatMessage(getMessage("object.not.set", getMessage("job"))));
        notNull(quartzDTO.getJob().getDetails(), formatMessage(getMessage("object.not.set", getMessage("job.detail"))));
        notNull(quartzDTO.getJob().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("job.key.name"))));
        notNull(quartzDTO.getJob().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));
        notNull(quartzDTO.getJob().getDetails().getDescription(), formatMessage(getMessage("object.not.set", getMessage("job.description"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notNull(quartzDTO.getApiJobData().getExecutorClass(), formatMessage(getMessage("object.not.set", getMessage("job.executor.class"))));

        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(quartzDTO));
        notNull(jobDataMap, formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));
        notEmpty(jobDataMap, formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        JobKey jobKey = quartzScheduler.getJobKeys(jobGroupEquals(quartzDTO.getJob().getDetails().getGroupName()))
                .stream()
                .filter(Objects::nonNull)
                .filter(it -> it.getName().equalsIgnoreCase(quartzDTO.getJob().getDetails().getKeyName()))
                .findFirst()
                .orElse(null);
        notNull(jobKey, formatMessage(getMessage("object.not.set", getMessage("job.key"))));

        JobBuilder jobBuilder = newJob(getExecutorClass(quartzDTO.getApiJobData().getExecutorClass()))
                .requestRecovery(quartzDTO.getJob().getRecover())
                .storeDurably(quartzDTO.getJob().getDurability())
                .withIdentity(jobKey)
                .withDescription(quartzDTO.getJob().getDetails().getDescription())
                .setJobData(jobDataMap);
        notNull(jobBuilder, formatMessage(getMessage("object.not.set", getMessage("job.builder"))));

        JobDetail jobDetail = jobBuilder.build();
        notNull(jobDetail, formatMessage(getMessage("object.not.set", getMessage("job.detail"))));

        quartzScheduler.addJob(jobDetail, false, quartzDTO.getJob().getDurability() ? FALSE : TRUE);
    }

    /**
     * Update existing simple trigger date.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Date updateExistingSimpleTrigger(QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException, IllegalStateException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getJob(), formatMessage(getMessage("object.not.set", getMessage("job"))));
        notNull(quartzDTO.getJob().getDetails(), formatMessage(getMessage("object.not.set", getMessage("job.detail"))));
        notNull(quartzDTO.getJob().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("job.key.name"))));
        notNull(quartzDTO.getJob().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("trigger.key.name"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("trigger.group.name"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getDescription(), formatMessage(getMessage("object.not.set", getMessage("trigger.description"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatType(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.type"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.interval"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.value"))));
        state(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue() > 0, formatMessage(getMessage("simple.job.scheduler.repeat.value.greater.than", 0)));

        TriggerKey triggerKey = quartzScheduler.getTriggerKeys(triggerGroupEquals(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getGroupName()))
                .stream()
                .filter(Objects::nonNull)
                .filter(it -> it.getName().equalsIgnoreCase(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getKeyName()))
                .findFirst()
                .orElse(null);
        notNull(triggerKey, formatMessage(getMessage("object.not.set", getMessage("trigger.key"))));

        JobKey jobKey = quartzScheduler.getJobKeys(jobGroupEquals(quartzDTO.getJob().getDetails().getGroupName()))
                .stream()
                .filter(Objects::nonNull)
                .filter(it -> it.getName().equalsIgnoreCase(quartzDTO.getJob().getDetails().getKeyName()))
                .findFirst()
                .orElse(null);
        notNull(jobKey, formatMessage(getMessage("object.not.set", getMessage("job.key"))));

        JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        notNull(jobDetail, formatMessage(getMessage("object.not.set", getMessage("job.detail"))));
        notNull(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));
        notEmpty(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getDescription())
                .withIdentity(triggerKey);
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        if (isTrue(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartNow())) {
            triggerBuilder = triggerBuilder.startNow();
        } else {
            notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartTime());
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartTime());
        }
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        if (nonNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getEndTime())) {
            triggerBuilder = triggerBuilder.endAt(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getEndTime());
        }
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        triggerBuilder = getSimpleTriggerBuilder(quartzDTO, triggerBuilder);
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        return quartzScheduler.scheduleJob(triggerBuilder.build());
    }

    /**
     * Update existing cron trigger date.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Date updateExistingCronTrigger(QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notNull(quartzDTO.getApiJobData().getExecutorClass(), formatMessage(getMessage("object.not.set", getMessage("job.executor.class"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler(), formatMessage(getMessage("object.not.set", getMessage("cron.job.scheduler"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("trigger.key.name"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("trigger.group.name"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getDescription(), formatMessage(getMessage("object.not.set", getMessage("trigger.description"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getCronExpression(), formatMessage(getMessage("object.not.set", getMessage("cron.expression"))));

        TriggerKey triggerKey = quartzScheduler.getTriggerKeys(triggerGroupEquals(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails().getGroupName()))
                .stream()
                .filter(Objects::nonNull)
                .filter(it -> it.getName().equalsIgnoreCase(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getKeyName()))
                .findFirst()
                .orElse(null);
        notNull(triggerKey, formatMessage(getMessage("object.not.set", getMessage("trigger.key"))));

        JobKey jobKey = quartzScheduler.getJobKeys(jobGroupEquals(quartzDTO.getJob().getDetails().getGroupName()))
                .stream()
                .filter(Objects::nonNull)
                .filter(it -> it.getName().equalsIgnoreCase(quartzDTO.getJob().getDetails().getKeyName()))
                .findFirst()
                .orElse(null);
        notNull(jobKey, formatMessage(getMessage("object.not.set", getMessage("job.key"))));

        JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        notNull(jobDetail, formatMessage(getMessage("object.not.set", getMessage("job.detail"))));
        notNull(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));
        notEmpty(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        TriggerBuilder triggerBuilder = getCronTriggerBuilder(quartzDTO, jobDetail, triggerKey);
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        if (isTrue(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartNow())) {
            triggerBuilder = triggerBuilder.startNow();
        } else {
            notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartTime(), formatMessage(getMessage("object.not.set", getMessage("trigger.start.time"))));
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartTime());
        }
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        if (nonNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getEndTime())) {
            triggerBuilder = triggerBuilder.endAt(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getEndTime());
            notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));
        }

        return quartzScheduler.scheduleJob(triggerBuilder.build());
    }

    /**
     * Fetch job details by group name list.
     *
     * @param jobGroupName the group name
     * @return the list
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public List<JobDetailsCO> fetchJobDetailsByJobGroupName(String jobGroupName) throws SchedulerException, IllegalArgumentException, NullPointerException {
        hasText(jobGroupName, formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));

        Set<JobKey> jobKeys = quartzScheduler.getJobKeys(jobGroupEquals(jobGroupName));
        notEmpty(jobKeys, formatMessage(getMessage("object.not.set", getMessage("job.key"))));

        List<JobDetailsCO> jobDetails = new ArrayList<JobDetailsCO>();
        jobKeys = jobKeys.parallelStream().sorted(((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))).collect(toSet());

        for (JobKey jobKey : jobKeys) {
            JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
            notNull(jobDetail, formatMessage(getMessage("object.not.set", getMessage("job.detail"))));

            JobExecutorClass jobExecutorClass = findJobExecutorClassByValue(jobDetail.getJobClass());
            notNull(jobExecutorClass, formatMessage(getMessage("object.not.set", getMessage("job.executor.class"))));

            Map<String, Object> jobDataMap = createMapFromJobDataMap(jobDetail.getJobDataMap());
            notEmpty(jobDataMap, formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

            jobDetails.add(new JobDetailsCO(
                    jobKey.getName(),
                    jobKey.getGroup(),
                    jobDetail.getDescription(),
                    jobExecutorClass,
                    jobDataMap,
                    jobDetail.isDurable(),
                    countTriggersOfJob(jobKey) == 0,
                    jobDetail.requestsRecovery(),
                    jobDetail.isConcurrentExectionDisallowed(),
                    jobDetail.isConcurrentExectionDisallowed()));
        }

        notEmpty(jobDetails, formatMessage(getMessage("object.not.set", getMessage("job.details"))));

        return jobDetails;
    }

    /**
     * Fetch trigger details by job key name and group name list.
     *
     * @param jobKeyName   the job key name
     * @param jobGroupName the group name
     * @return the list
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    @SuppressWarnings("unchecked")
    public List<TriggerDetailsCO> fetchTriggerDetailsByJobKeyNameAndJobGroupName(String jobKeyName, String jobGroupName) throws SchedulerException, IllegalArgumentException, NullPointerException {
        hasText(jobKeyName, formatMessage(getMessage("object.not.set", getMessage("job.key.name"))));
        hasText(jobGroupName, formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));

        JobKey jobKey = new JobKey(jobKeyName, jobGroupName);
        notNull(jobKey, formatMessage(getMessage("object.not.set", getMessage("job.key"))));

        List<Trigger> triggers = (List<Trigger>) quartzScheduler.getTriggersOfJob(jobKey);
        notEmpty(triggers, formatMessage(getMessage("object.not.set", getMessage("trigger"))));

        triggers = triggers.stream().sorted((o1, o2) -> o1.getKey().getName().compareToIgnoreCase(o2.getKey().getName())).collect(toList());

        List<TriggerDetailsCO> triggerDetails = new ArrayList<TriggerDetailsCO>();
        for (Trigger trigger : triggers) {
            TriggerDetailsCO triggerDetail = new TriggerDetailsCO(
                    trigger.getKey().getName(),
                    trigger.getKey().getGroup(),
                    trigger.getDescription(),
                    trigger.getStartTime(),
                    trigger.getNextFireTime(),
                    trigger.getPreviousFireTime(),
                    trigger.getEndTime(),
                    trigger.getFinalFireTime(),
                    trigger.getPriority(),
                    quartzScheduler.getTriggerState(triggerKey(trigger.getKey().getName(), trigger.getKey().getGroup())).toString());
            triggerDetails.add(triggerDetail);

            if (trigger instanceof SimpleTrigger) {
                triggerDetail.setType(SIMPLE);
                TriggerDetailsCO.SimpleTriggerDetails simpleTriggerDetails = triggerDetail.new SimpleTriggerDetails();
                simpleTriggerDetails.setCountTriggered(((SimpleTrigger) trigger).getTimesTriggered());
                simpleTriggerDetails.setRepeatCount(((SimpleTrigger) trigger).getRepeatCount());
                simpleTriggerDetails.setRepeatInterval(((SimpleTrigger) trigger).getRepeatInterval());
            } else if (trigger instanceof CronTrigger) {
                triggerDetail.setType(CRON);
                TriggerDetailsCO.CronTriggerDetails cronTriggerDetails = triggerDetail.new CronTriggerDetails();
                cronTriggerDetails.setCronExpression(((CronTrigger) trigger).getCronExpression());
                cronTriggerDetails.setExpressionSummary(((CronTrigger) trigger).getExpressionSummary());
                cronTriggerDetails.setTimeZone(((CronTrigger) trigger).getTimeZone());
            }
        }
        notEmpty(triggerDetails, formatMessage(getMessage("object.not.set", getMessage("trigger.details"))));

        return triggerDetails;
    }

    /**
     * Fetch quartz details for a group name list.
     *
     * @param jobGroupName the group name
     * @return the list
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public List<QuartzDetailsCO> fetchQuartzDetailsForJobGroupName(String jobGroupName) throws SchedulerException, IllegalArgumentException, NullPointerException {
        hasText(jobGroupName, formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));

        List<JobDetailsCO> jobDetails = fetchJobDetailsByJobGroupName(jobGroupName);
        notEmpty(jobDetails, formatMessage(getMessage("object.not.set", getMessage("job.details"))));

        jobDetails = jobDetails.stream().sorted(((o1, o2) -> o1.getKeyName().compareToIgnoreCase(o2.getKeyName()))).collect(toList());

        List<QuartzDetailsCO> quartzDetails = new ArrayList<QuartzDetailsCO>();
        for (JobDetailsCO jobDetail : jobDetails) {
            List<TriggerDetailsCO> triggerDetailsCOS = fetchTriggerDetailsByJobKeyNameAndJobGroupName(jobDetail.getKeyName(), jobDetail.getGroupName());
            notEmpty(triggerDetailsCOS, formatMessage(getMessage("object.not.set", getMessage("trigger.details"))));

            quartzDetails.add(new QuartzDetailsCO(jobDetail, triggerDetailsCOS));
        }
        notEmpty(quartzDetails, formatMessage(getMessage("object.not.set", getMessage("quartz.details"))));

        return quartzDetails;
    }

    /**
     * Resume triggers.
     *
     * @param triggerKeyName   the key name
     * @param triggerGroupName the group name
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public void resumeTriggers(String triggerKeyName, String triggerGroupName) throws SchedulerException, IllegalArgumentException, NullPointerException {
        hasText(triggerGroupName, formatMessage(getMessage("object.not.set", getMessage("trigger.group.name"))));

        if (isNotEmpty(triggerKeyName)) {
            quartzScheduler.resumeTrigger(new TriggerKey(triggerKeyName, triggerGroupName));
        } else {
            quartzScheduler.resumeTriggers(triggerGroupEquals(triggerGroupName));
        }
    }

    /**
     * Pause triggers.
     *
     * @param triggerKeyName   the key name
     * @param triggerGroupName the group name
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public void pauseTriggers(String triggerKeyName, String triggerGroupName) throws SchedulerException, IllegalArgumentException, NullPointerException {
        hasText(triggerGroupName, formatMessage(getMessage("object.not.set", getMessage("trigger.group.name"))));

        if (isNotEmpty(triggerKeyName)) {
            quartzScheduler.pauseTrigger(new TriggerKey(triggerKeyName, triggerGroupName));
        } else {
            quartzScheduler.pauseTriggers(triggerGroupEquals(triggerGroupName));
        }
    }

    /**
     * Resume jobs.
     *
     * @param jobKeyName   the key name
     * @param jobGroupName the group name
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public void resumeJobs(String jobKeyName, String jobGroupName) throws SchedulerException, IllegalArgumentException, NullPointerException {
        hasText(jobGroupName, formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));

        if (isNotEmpty(jobKeyName)) {
            quartzScheduler.resumeJob(new JobKey(jobKeyName, jobGroupName));
        } else {
            quartzScheduler.resumeJobs(jobGroupEquals(jobGroupName));
        }
    }

    /**
     * Pause jobs.
     *
     * @param jobKeyName   the key name
     * @param jobGroupName the group name
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public void pauseJobs(String jobKeyName, String jobGroupName) throws SchedulerException, IllegalArgumentException, NullPointerException {
        hasText(jobGroupName, formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));

        if (isNotEmpty(jobKeyName)) {
            quartzScheduler.pauseJob(new JobKey(jobKeyName, jobGroupName));
        } else {
            quartzScheduler.pauseJobs(jobGroupEquals(jobGroupName));
        }
    }

    /**
     * Delete jobs boolean.
     *
     * @param jobKeyName   the key name
     * @param jobGroupName the group name
     * @return the boolean
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Boolean deleteJobs(String jobKeyName, String jobGroupName) throws SchedulerException, IllegalArgumentException, NullPointerException {
        hasText(jobGroupName, formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));

        Boolean deleted = FALSE;
        if (isNotEmpty(jobKeyName)) {
            JobKey jobKey = new JobKey(jobKeyName, jobGroupName);
            notNull(jobKey, formatMessage(getMessage("object.not.set", getMessage("job.key"))));

            deleted = quartzScheduler.deleteJob(jobKey);
        } else {
            Set<JobKey> jobKeys = quartzScheduler.getJobKeys(jobGroupEquals(jobGroupName));
            notEmpty(jobKeys, formatMessage(getMessage("object.not.set", getMessage("job.key"))));

            deleted = quartzScheduler.deleteJobs(jobKeys.stream().filter(Objects::nonNull).collect(toList()));
        }
        isTrue(deleted, formatMessage(getMessage("error.while.deleting.job")));

        return deleted;
    }

    /**
     * Delete triggers boolean.
     *
     * @param triggerKeyName   the key name
     * @param triggerGroupName the group name
     * @return the boolean
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Boolean deleteTriggers(String triggerKeyName, String triggerGroupName) throws SchedulerException, IllegalArgumentException, NullPointerException {
        hasText(triggerGroupName, formatMessage(getMessage("object.not.set", getMessage("trigger.group.name"))));

        Boolean deleted = FALSE;
        if (isNotEmpty(triggerKeyName)) {
            TriggerKey triggerKey = new TriggerKey(triggerKeyName, triggerGroupName);
            notNull(triggerKey, formatMessage(getMessage("object.not.set", getMessage("trigger.key"))));

            deleted = quartzScheduler.unscheduleJob(triggerKey);
        } else {
            Set<TriggerKey> triggerKeys = quartzScheduler.getTriggerKeys(triggerGroupEquals(triggerGroupName));
            notEmpty(triggerKeys, formatMessage(getMessage("object.not.set", getMessage("trigger.key"))));

            deleted = quartzScheduler.unscheduleJobs(triggerKeys.stream().filter(Objects::nonNull).collect(toList()));
        }
        isTrue(deleted, formatMessage(getMessage("error.while.deleting.trigger")));

        return deleted;
    }

    private Integer countTriggersOfJob(JobKey jobKey) throws SchedulerException, IllegalArgumentException {
        notNull(jobKey, formatMessage(getMessage("object.not.set", getMessage("job.key"))));
        List<? extends Trigger> triggersOfAJob = quartzScheduler.getTriggersOfJob(jobKey);
        return isEmpty(triggersOfAJob) ? 0 : triggersOfAJob.size();
    }

    private Map<String, String> createJobDataMap(QuartzDTO quartzDTO) throws IllegalArgumentException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getJob(), formatMessage(getMessage("object.not.set", getMessage("job"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notEmpty(quartzDTO.getApiJobData().getRequestHeaders(), formatMessage(getMessage("object.not.set", getMessage("request.headers"))));

        Map<String, String> jobData = new HashMap<String, String>();
        quartzDTO.getApiJobData().getRequestHeaders().stream().filter(Objects::nonNull).forEach(it -> {
            jobData.put("header_" + it.getKey(), it.getValue());
        });

        hasText(quartzDTO.getApiJobData().getRequestUrl(), formatMessage(getMessage("object.not.set", getMessage("request.url"))));
        jobData.put("request-url", quartzDTO.getApiJobData().getRequestUrl());

        notNull(quartzDTO.getApiJobData().getRequestType(), formatMessage(getMessage("object.not.set", getMessage("request.type"))));
        jobData.put("request-type", quartzDTO.getApiJobData().getRequestType().name());

        return jobData;
    }

    private Map<String, Object> createMapFromJobDataMap(JobDataMap jobDataMap) throws IllegalArgumentException {
        notNull(jobDataMap, formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));
        notEmpty(jobDataMap, formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        Map<String, Object> map = new HashMap<String, Object>();
        jobDataMap.forEach(map::put);

        notEmpty(map, formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        return map;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Job> getExecutorClass(JobExecutorClass jobExecutorClass) throws ClassNotFoundException, IllegalArgumentException {
        notNull(jobExecutorClass, formatMessage(getMessage("object.not.set", getMessage("job.executor.class"))));
        return (Class<? extends Job>) forName(jobExecutorClass.getPackageName());
    }

    private JobDetail createJobDetail(QuartzDTO quartzDTO) throws ClassNotFoundException, IllegalArgumentException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getJob(), formatMessage(getMessage("object.not.set", getMessage("job"))));
        notNull(quartzDTO.getJob().getDetails(), formatMessage(getMessage("object.not.set", getMessage("job"))));
        notNull(quartzDTO.getJob().getDetails().getKeyName(), formatMessage(getMessage("object.not.set", getMessage("job.key.name"))));
        notNull(quartzDTO.getJob().getDetails().getGroupName(), formatMessage(getMessage("object.not.set", getMessage("job.group.name"))));
        hasText(quartzDTO.getJob().getDetails().getDescription(), formatMessage(getMessage("object.not.set", getMessage("job.description"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notNull(quartzDTO.getApiJobData().getExecutorClass(), formatMessage(getMessage("object.not.set", getMessage("job.executor.class"))));

        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(quartzDTO));
        notNull(jobDataMap, formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));
        notEmpty(jobDataMap, formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        JobKey jobKey = new JobKey(quartzDTO.getJob().getDetails().getKeyName(), quartzDTO.getJob().getDetails().getGroupName());
        notNull(jobKey, formatMessage(getMessage("object.not.set", getMessage("job.key"))));

        JobBuilder jobBuilder = newJob(getExecutorClass(quartzDTO.getApiJobData().getExecutorClass()))
                .requestRecovery(quartzDTO.getJob().getRecover())
                .storeDurably(quartzDTO.getJob().getDurability())
                .withIdentity(jobKey)
                .withDescription(quartzDTO.getJob().getDetails().getDescription())
                .setJobData(jobDataMap);
        notNull(jobBuilder, formatMessage(getMessage("object.not.set", getMessage("job.builder"))));

        JobDetail jobDetail = jobBuilder.build();
        notNull(jobDetail, formatMessage(getMessage("object.not.set", getMessage("job.detail"))));

        return jobDetail;
    }

    private TriggerBuilder getCronTriggerBuilder(QuartzDTO quartzDTO, JobDetail jobDetail, TriggerKey triggerKey) throws IllegalArgumentException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler(), formatMessage(getMessage("object.not.set", getMessage("cron.job.scheduler"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails(), formatMessage(getMessage("object.not.set", getMessage("trigger"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getDescription(), formatMessage(getMessage("object.not.set", getMessage("trigger.description"))));
        notNull(quartzDTO.getApiJobData().getCronJobScheduler().getCronExpression(), formatMessage(getMessage("object.not.set", getMessage("cron.expression"))));
        notNull(triggerKey, formatMessage(getMessage("object.not.set", getMessage("trigger.key"))));
        notNull(jobDetail, formatMessage(getMessage("object.not.set", getMessage("job.detail"))));
        notNull(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));
        notEmpty(jobDetail.getJobDataMap(), formatMessage(getMessage("object.not.set", getMessage("job.data.map"))));

        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getDescription())
                .withIdentity(triggerKey)
                .withSchedule(cronSchedule(quartzDTO.getApiJobData().getCronJobScheduler().getCronExpression()).inTimeZone(getDefault()));
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        return triggerBuilder;
    }

    private TriggerBuilder getSimpleTriggerBuilder(QuartzDTO quartzDTO, TriggerBuilder triggerBuilder) throws IllegalArgumentException, IllegalStateException {
        notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request"))));
        notNull(quartzDTO.getApiJobData(), formatMessage(getMessage("object.not.set", getMessage("api.job.data"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatType(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.type"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.interval"))));
        notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.value"))));

        switch (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatType()) {
            case REPEAT_BY_SECOND:
                if (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(repeatSecondlyForever(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()));
                } else {
                    notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatCount(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.count"))));
                    state(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatCount() > 0, formatMessage(getMessage("simple.job.scheduler.repeat.count.greater.than", 0)));

                    triggerBuilder = triggerBuilder.withSchedule(repeatSecondlyForTotalCount(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatCount(), quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()));
                }
                break;
            case REPEAT_BY_MINUTE:
                if (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(repeatMinutelyForever(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()));
                } else {
                    notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatCount(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.count"))));
                    state(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatCount() > 0, formatMessage(getMessage("simple.job.scheduler.repeat.count.greater.than", 0)));

                    triggerBuilder = triggerBuilder.withSchedule(repeatMinutelyForTotalCount(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatCount(), quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()));
                }
                break;
            case REPEAT_BY_HOUR:
                if (quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(repeatHourlyForever(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()));
                } else {
                    notNull(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatCount(), formatMessage(getMessage("object.not.set", getMessage("simple.job.scheduler.repeat.count"))));
                    state(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatCount() > 0, formatMessage(getMessage("simple.job.scheduler.repeat.count.greater.than", 0)));

                    triggerBuilder = triggerBuilder.withSchedule(repeatHourlyForTotalCount(quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatCount(), quartzDTO.getApiJobData().getSimpleJobScheduler().getRepeatInterval().getRepeatValue()));
                }
                break;
            default:
                triggerBuilder = null;
                break;
        }
        notNull(triggerBuilder, formatMessage(getMessage("object.not.set", getMessage("trigger.builder"))));

        return triggerBuilder;
    }

}
