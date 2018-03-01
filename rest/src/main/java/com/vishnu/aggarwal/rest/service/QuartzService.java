package com.vishnu.aggarwal.rest.service;

import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import com.vishnu.aggarwal.core.exceptions.*;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.StringUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
import static java.lang.Boolean.FALSE;
import static java.lang.Class.forName;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;
import static org.quartz.impl.matchers.GroupMatcher.triggerGroupEquals;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * The type Quartz service.
 */
@Service
@CommonsLog

public class QuartzService {

    /**
     * The Scheduler factory bean.
     */
    @Autowired
    SchedulerFactoryBean schedulerFactoryBean;

    /**
     * The Quartz scheduler.
     */
    @Autowired
    Scheduler quartzScheduler;

    /**
     * Create new unscheduled api job.
     *
     * @param quartzDTO the quartz dto
     * @throws ClassNotFoundException the class not found exception
     * @throws SchedulerException     the scheduler exception
     */
    public void createNewUnscheduledApiJob(QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException {
        JobKey jobKey = new JobKey(quartzDTO.getJob().getKeyName(), quartzDTO.getJob().getGroupName());
        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(quartzDTO));
        JobBuilder jobBuilder = newJob(getExecutorClass(quartzDTO.getExecutorClass()))
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

    /**
     * Create new scheduled api simple job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws ClassNotFoundException   the class not found exception
     * @throws SchedulerException       the scheduler exception
     * @throws JobNotScheduledException the job not scheduled exception
     */
    public Date createNewScheduledApiSimpleJob(QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, JobNotScheduledException {
        JobKey jobKey = new JobKey(quartzDTO.getJob().getKeyName(), quartzDTO.getJob().getGroupName());
        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(quartzDTO));
        JobBuilder jobBuilder = newJob(getExecutorClass(quartzDTO.getExecutorClass()))
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
        if (BooleanUtils.isTrue(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartNow())) {
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
        Date scheduledDate = quartzScheduler.scheduleJob(jobDetail, triggerBuilder.build());

        if (isNull(scheduledDate)) {
            throw new JobNotScheduledException();
        }

        return scheduledDate;
    }

    /**
     * Create new scheduled api cron job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws ClassNotFoundException   the class not found exception
     * @throws SchedulerException       the scheduler exception
     * @throws JobNotScheduledException the job not scheduled exception
     */
    public Date createNewScheduledApiCronJob(QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, JobNotScheduledException {
        JobDataMap jobDataMap = new JobDataMap(createJobDataMap(quartzDTO));
        JobBuilder jobBuilder = newJob(getExecutorClass(quartzDTO.getExecutorClass()))
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
                .withSchedule(cronSchedule(quartzDTO.getApiJobData().getCronJobScheduler().getCronExpression()).inTimeZone(TimeZone.getDefault()));
        if (BooleanUtils.isTrue(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartNow())) {
            triggerBuilder = triggerBuilder.startNow();
        } else {
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartTime());
        }

        Date scheduledDate = quartzScheduler.scheduleJob(jobDetail, triggerBuilder.build());

        if (isNull(scheduledDate)) {
            throw new JobNotScheduledException();
        }

        return scheduledDate;
    }

    /**
     * Create new simple trigger for job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws SchedulerException           the scheduler exception
     * @throws TriggerNotScheduledException the trigger not scheduled exception
     */
    public Date createNewSimpleTriggerForJob(QuartzDTO quartzDTO) throws SchedulerException, TriggerNotScheduledException {
        JobKey jobKey = new JobKey(quartzDTO.getJob().getKeyName(), quartzDTO.getJob().getGroupName());
        JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getTriggerDescription())
                .withIdentity(new TriggerKey(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getKeyName(), quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getGroupName()));
        if (BooleanUtils.isTrue(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getStartNow())) {
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

        Date scheduledDate = quartzScheduler.scheduleJob(triggerBuilder.build());

        if (isNull(scheduledDate)) {
            throw new TriggerNotScheduledException();
        }

        return scheduledDate;
    }

    /**
     * Create new cron trigger for job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws SchedulerException           the scheduler exception
     * @throws TriggerNotScheduledException the trigger not scheduled exception
     */
    public Date createNewCronTriggerForJob(QuartzDTO quartzDTO) throws SchedulerException, TriggerNotScheduledException {
        JobKey jobKey = new JobKey(quartzDTO.getJob().getKeyName(), quartzDTO.getJob().getGroupName());
        JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getTriggerDescription())
                .withIdentity(new TriggerKey(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getKeyName(), quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getGroupName()))
                .withSchedule(cronSchedule(quartzDTO.getApiJobData().getCronJobScheduler().getCronExpression()).inTimeZone(TimeZone.getDefault()));
        if (BooleanUtils.isTrue(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartNow())) {
            triggerBuilder = triggerBuilder.startNow();
        } else {
            triggerBuilder = triggerBuilder.startAt(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getStartTime());
        }

        Date scheduledDate = quartzScheduler.scheduleJob(triggerBuilder.build());

        if (isNull(scheduledDate)) {
            throw new TriggerNotScheduledException();
        }

        return scheduledDate;
    }

    /**
     * Fetch job details by group name list.
     *
     * @param groupName the group name
     * @return the list
     * @throws SchedulerException         the scheduler exception
     * @throws JobDetailNotFoundException the job detail not found exception
     */
    public List<JobDetailsCO> fetchJobDetailsByGroupName(String groupName) throws SchedulerException, JobDetailNotFoundException {
        if (StringUtils.isEmpty(groupName)) {
            throw new JobDetailNotFoundException();
        }

        Set<JobKey> jobKeys = quartzScheduler.getJobKeys(jobGroupEquals(groupName));

        if (isEmpty(jobKeys)) {
            throw new JobDetailNotFoundException();
        }

        List<JobDetailsCO> jobDetails = new ArrayList<JobDetailsCO>();
        for (JobKey jobKey : jobKeys) {
            JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
            if (nonNull(jobDetail)) {
                jobDetails.add(new JobDetailsCO(
                        jobKey.getName(),
                        jobKey.getGroup(),
                        jobDetail.getDescription(),
                        JobExecutorClass.findJobExecutorClassByValue(jobDetail.getJobClass()),
                        createMapFromJobDataMap(jobDetail.getJobDataMap()),
                        jobDetail.isDurable(),
                        countTriggersOfJob(jobKey) == 0,
                        jobDetail.requestsRecovery(),
                        jobDetail.isConcurrentExectionDisallowed(),
                        jobDetail.isConcurrentExectionDisallowed()));
            }
        }

        if (isEmpty(jobDetails)) {
            throw new JobDetailNotFoundException();
        }

        jobDetails.sort((o1, o2) -> o1.getKeyName().compareToIgnoreCase(o2.getKeyName()));
        return jobDetails;
    }

    /**
     * Fetch trigger details by job key name and group name list.
     *
     * @param jobKeyName the job key name
     * @param groupName  the group name
     * @return the list
     * @throws SchedulerException             the scheduler exception
     * @throws TriggerDetailNotFoundException the trigger detail not found exception
     */
    @SuppressWarnings("unchecked")
    public List<TriggerDetailsCO> fetchTriggerDetailsByJobKeyNameAndGroupName(String jobKeyName, String groupName) throws SchedulerException, TriggerDetailNotFoundException {
        if (StringUtils.isEmpty(groupName)) {
            throw new TriggerDetailNotFoundException();
        }

        List<Trigger> triggers = (List<Trigger>) quartzScheduler.getTriggersOfJob(new JobKey(jobKeyName, groupName));

        if (isEmpty(triggers)) {
            throw new TriggerDetailNotFoundException();
        }

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
                    quartzScheduler.getTriggerState(TriggerKey.triggerKey(trigger.getKey().getName(), trigger.getKey().getGroup())).toString());
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

        if (isEmpty(triggerDetails)) {
            throw new TriggerDetailNotFoundException();
        }

        triggerDetails.sort(((o1, o2) -> o1.getKeyName().compareToIgnoreCase(o2.getKeyName())));
        return triggerDetails;
    }

    /**
     * Fetch quartz details for a group name list.
     *
     * @param groupName the group name
     * @return the list
     * @throws SchedulerException             the scheduler exception
     * @throws JobDetailNotFoundException     the job detail not found exception
     * @throws QuartzDetailNotFoundException  the quartz detail not found exception
     * @throws TriggerDetailNotFoundException the trigger detail not found exception
     */
    public List<QuartzDetailsCO> fetchQuartzDetailsForAGroupName(String groupName) throws SchedulerException, JobDetailNotFoundException, QuartzDetailNotFoundException, TriggerDetailNotFoundException {
        if (StringUtils.isEmpty(groupName)) {
            throw new QuartzDetailNotFoundException();
        }

        List<JobDetailsCO> jobDetails = fetchJobDetailsByGroupName(groupName);

        if (isEmpty(jobDetails)) {
            throw new QuartzDetailNotFoundException();
        }

        List<QuartzDetailsCO> quartzDetails = new ArrayList<QuartzDetailsCO>();
        for (JobDetailsCO jobDetail : jobDetails) {
            QuartzDetailsCO quartzDetail = new QuartzDetailsCO();
            quartzDetail.setJobDetails(jobDetail);
            quartzDetail.setTriggerDetails(fetchTriggerDetailsByJobKeyNameAndGroupName(jobDetail.getKeyName(), jobDetail.getGroupName()));
        }

        if (isEmpty(quartzDetails)) {
            throw new QuartzDetailNotFoundException();
        }

        quartzDetails.sort(((o1, o2) -> o1.getJobDetails().getKeyName().compareToIgnoreCase(o2.getJobDetails().getKeyName())));
        return quartzDetails;
    }

    /**
     * Resume triggers.
     *
     * @param keyName   the key name
     * @param groupName the group name
     * @throws SchedulerException the scheduler exception
     */
    public void resumeTriggers(String keyName, String groupName) throws SchedulerException, ResumeTriggerFailureException {
        if (StringUtils.isEmpty(groupName)) {
            throw new ResumeTriggerFailureException();
        }

        if (isNotEmpty(keyName)) {
            quartzScheduler.resumeTrigger(new TriggerKey(keyName, groupName));
        } else {
            quartzScheduler.resumeTriggers(triggerGroupEquals(groupName));
        }
    }

    /**
     * Pause triggers.
     *
     * @param keyName   the key name
     * @param groupName the group name
     * @throws SchedulerException the scheduler exception
     */
    public void pauseTriggers(String keyName, String groupName) throws SchedulerException, PauseTriggerFailureException {
        if (StringUtils.isEmpty(groupName)) {
            throw new PauseTriggerFailureException();
        }

        if (isNotEmpty(keyName)) {
            quartzScheduler.pauseTrigger(new TriggerKey(keyName, groupName));
        } else {
            quartzScheduler.pauseTriggers(triggerGroupEquals(groupName));
        }
    }

    /**
     * Resume jobs.
     *
     * @param keyName   the key name
     * @param groupName the group name
     * @throws SchedulerException the scheduler exception
     */
    public void resumeJobs(String keyName, String groupName) throws SchedulerException, ResumeJobFailureException {
        if (StringUtils.isEmpty(groupName)) {
            throw new ResumeJobFailureException();
        }

        if (isNotEmpty(keyName)) {
            quartzScheduler.resumeJob(new JobKey(keyName, groupName));
        } else {
            quartzScheduler.resumeJobs(jobGroupEquals(groupName));
        }
    }

    /**
     * Pause jobs.
     *
     * @param keyName   the key name
     * @param groupName the group name
     * @throws SchedulerException the scheduler exception
     */
    public void pauseJobs(String keyName, String groupName) throws SchedulerException, PauseJobFailureException {
        if (StringUtils.isEmpty(groupName)) {
            throw new PauseJobFailureException();
        }

        if (isNotEmpty(keyName)) {
            quartzScheduler.pauseJob(new JobKey(keyName, groupName));
        } else {
            quartzScheduler.pauseJobs(jobGroupEquals(groupName));
        }
    }

    /**
     * Delete jobs boolean.
     *
     * @param keyName   the key name
     * @param groupName the group name
     * @return the boolean
     * @throws SchedulerException        the scheduler exception
     * @throws JobDeleteFailureException the job delete failure exception
     */
    public Boolean deleteJobs(String keyName, String groupName) throws SchedulerException, JobDeleteFailureException {

        if (StringUtils.isEmpty(groupName)) {
            throw new JobDeleteFailureException();
        }

        Boolean deleted = FALSE;
        if (isNotEmpty(keyName)) {
            deleted = quartzScheduler.deleteJob(new JobKey(keyName, groupName));
        } else {
            deleted = quartzScheduler.deleteJobs(quartzScheduler.getJobKeys(jobGroupEquals(groupName)).stream().filter(Objects::nonNull).collect(toList()));
        }

        if (isFalse(deleted)) {
            throw new JobDeleteFailureException();
        }

        return deleted;
    }

    /**
     * Delete triggers boolean.
     *
     * @param keyName   the key name
     * @param groupName the group name
     * @return the boolean
     * @throws SchedulerException            the scheduler exception
     * @throws TriggerDeleteFailureException the trigger delete failure exception
     */
    public Boolean deleteTriggers(String keyName, String groupName) throws SchedulerException, TriggerDeleteFailureException {

        if (StringUtils.isEmpty(groupName)) {
            throw new TriggerDeleteFailureException();
        }

        Boolean deleted = FALSE;
        if (isNotEmpty(keyName)) {
            deleted = quartzScheduler.unscheduleJob(new TriggerKey(keyName, groupName));
        } else {
            deleted = quartzScheduler.unscheduleJobs(quartzScheduler.getTriggerKeys(triggerGroupEquals(groupName)).stream().filter(Objects::nonNull).collect(toList()));
        }

        if (isFalse(deleted)) {
            throw new TriggerDeleteFailureException();
        }

        return deleted;
    }

    private Integer countTriggersOfJob(JobKey jobKey) throws SchedulerException {
        List<? extends Trigger> triggersOfAJob = quartzScheduler.getTriggersOfJob(jobKey);
        return isEmpty(triggersOfAJob) ? 0 : triggersOfAJob.size();
    }

    private Map<String, String> createJobDataMap(QuartzDTO quartzDTO) {
        Map<String, String> jobData = new HashMap<String, String>();
        if (nonNull(quartzDTO) && nonNull(quartzDTO.getApiJobData())) {
            if (!isEmpty(quartzDTO.getApiJobData().getRequestHeaders())) {
                quartzDTO.getApiJobData().getRequestHeaders().stream().filter(Objects::nonNull).forEach(it -> {
                    jobData.put("header_" + it.getKey(), it.getValue());
                });
            }
            jobData.put("request-url", quartzDTO.getApiJobData().getRequestUrl());
        }
        return jobData;
    }

    private Map<String, Object> createMapFromJobDataMap(JobDataMap jobDataMap) {
        Map<String, Object> map = new HashMap<String, Object>();
        if (nonNull(jobDataMap) && !isEmpty(jobDataMap.keySet())) {
            jobDataMap.forEach(map::put);
        }
        return map;
    }

    @SuppressWarnings("unchecked")
    private Class<? extends Job> getExecutorClass(JobExecutorClass jobExecutorClass) throws ClassNotFoundException {
        return (Class<? extends Job>) forName(jobExecutorClass.getPackageName());
    }
}
