package com.vishnu.aggarwal.rest.service;

import com.vishnu.aggarwal.core.co.*;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.core.exceptions.InvalidRequestException;
import com.vishnu.aggarwal.core.exceptions.JobDetailNotFoundException;
import com.vishnu.aggarwal.core.service.BaseService;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.enums.JobExecutorClass.findJobExecutorClassByValue;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
import static com.vishnu.aggarwal.rest.util.DTOConversion.convertFromUser;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Class.forName;
import static java.lang.String.format;
import static java.util.Collections.singletonList;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.TimeZone.getDefault;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.BooleanUtils.isFalse;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.exception.ExceptionUtils.printRootCauseStackTrace;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
import static org.quartz.JobKey.jobKey;
import static org.quartz.SimpleScheduleBuilder.*;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;
import static org.quartz.impl.matchers.GroupMatcher.jobGroupEquals;
import static org.quartz.impl.matchers.GroupMatcher.triggerGroupEquals;
import static org.springframework.util.CollectionUtils.isEmpty;

/**
 * The type Quartz service.
 */
@Service
@CommonsLog
@Transactional
public class QuartzService extends BaseService {

    /**
     * The Quartz scheduler.
     */
    private final Scheduler quartzScheduler;

    private final UserService userService;

    /**
     * Instantiates a new Quartz service.
     *
     * @param quartzScheduler the quartz scheduler
     * @param userService     the user service
     */
    @Autowired
    public QuartzService(
            Scheduler quartzScheduler,
            com.vishnu.aggarwal.rest.service.UserService userService) {
        this.quartzScheduler = quartzScheduler;
        this.userService = userService;
    }


    public Map<KeyGroupDescriptionDTO, Date> createOrUpdateJob(@NotBlank @NotNull @NotEmpty final QuartzDTO quartzDTO) throws SchedulerException, NullPointerException, ClassNotFoundException, IllegalArgumentException, InvalidRequestException {
        final JobDetail jobDetail = createJobDetail(quartzDTO.getJob());
        if (isEmpty(quartzDTO.getTriggers()) && isFalse(quartzDTO.getJob().getScheduled())) {
            log.info("Creating unscheduled job");
            quartzScheduler.addJob(
                    jobDetail,
                    quartzDTO.getJob().getReplace(),
                    quartzDTO.getJob().getDurability() ? FALSE : TRUE
            );
            return null;
        } else if (!isEmpty(quartzDTO.getTriggers()) && isTrue(quartzDTO.getJob().getScheduled())) {
            log.info("Creating scheduled job");
            Map<KeyGroupDescriptionDTO, Date> scheduledJobs = new HashMap<>();
            for (TriggerCO it : quartzDTO.getTriggers()) {
                Trigger trigger = getTriggerBuilder(it)
                        .forJob(jobDetail.getKey())
                        .usingJobData(jobDetail.getJobDataMap())
                        .build();
                scheduledJobs.put(
                        new KeyGroupDescriptionDTO(
                                trigger.getKey().getName(),
                                convertFromUser(userService.findById(trigger.getKey().getGroup())),
                                trigger.getDescription()
                        ),
                        quartzScheduler.scheduleJob(jobDetail, trigger));
            }
            return scheduledJobs;
        } else {
            throw new InvalidRequestException("");
        }
    }

    public Map<KeyGroupDescriptionDTO, Date> createOrUpdateTrigger(@NotBlank @NotNull @NotEmpty final QuartzDTO quartzDTO) throws SchedulerException, InvalidRequestException, JobDetailNotFoundException {
        final JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey(quartzDTO.getJob().getDetails().getKey(), quartzDTO.getJob().getDetails().getGroup().getId().toString()));
        if (isNull(jobDetail)) {
            throw new JobDetailNotFoundException("");
        }
        if (quartzDTO.getTriggers().isEmpty()) {
            throw new InvalidRequestException("");
        }

        Map<KeyGroupDescriptionDTO, Date> scheduledJobs = new HashMap<>();
        for (TriggerCO it : quartzDTO.getTriggers()) {
            Trigger trigger = getTriggerBuilder(it)
                    .forJob(jobDetail.getKey())
                    .usingJobData(jobDetail.getJobDataMap())
                    .build();
            scheduledJobs.put(
                    new KeyGroupDescriptionDTO(
                            trigger.getKey().getName(),
                            convertFromUser(userService.findById(trigger.getKey().getGroup())),
                            trigger.getDescription()
                    ),
                    quartzScheduler.scheduleJob(jobDetail, trigger));
        }
        return scheduledJobs;
    }

    public List<JobDetailsCO> fetchJobDetails() throws SchedulerException, IllegalArgumentException, NullPointerException {
        List<JobDetailsCO> jobDetails = new ArrayList<JobDetailsCO>();
        for (JobKey jobKey : quartzScheduler.getJobKeys(jobGroupEquals(userService.getCurrentLoggedInUserId().toString()))
                .parallelStream()
                .sorted(((JobKey jk1, JobKey jk2) -> jk1.getName().compareToIgnoreCase(jk2.getName())))
                .collect(toList())) {
            final JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
            jobDetails.add(new JobDetailsCO(
                    new KeyGroupDescriptionDTO(jobKey.getName(), convertFromUser(userService.findById(jobKey.getGroup())), jobDetail.getDescription()),
                    findJobExecutorClassByValue(jobDetail.getJobClass()),
                    jobDetail.getJobDataMap().getWrappedMap(),
                    jobDetail.isDurable(),
                    countTriggersOfJob(jobKey) == 0,
                    jobDetail.requestsRecovery(),
                    jobDetail.isConcurrentExectionDisallowed(),
                    jobDetail.isPersistJobDataAfterExecution()
            ));
        }
        return jobDetails;
    }

    public List<TriggerDetailsCO> fetchTriggerDetailsByJobKeyGroupName(@NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        if(isNull(keyGroupDescriptionDTO.getGroup())) {
            keyGroupDescriptionDTO.setGroup(convertFromUser(userService.getCurrentLoggedInUser()));
        }

        List<TriggerDetailsCO> triggerDetails = new ArrayList<TriggerDetailsCO>();
        for (Trigger trigger : quartzScheduler.getTriggersOfJob(jobKey(keyGroupDescriptionDTO.getKey(), keyGroupDescriptionDTO.getGroup().getId().toString()))
                .stream()
                .sorted((Trigger t1, Trigger t2) -> t1.getKey().getName().compareToIgnoreCase(t2.getKey().getName()))
                .collect(toList())) {
            TriggerDetailsCO triggerDetail = new TriggerDetailsCO(
                    new KeyGroupDescriptionDTO(trigger.getKey().getName(), convertFromUser(userService.findById(trigger.getKey().getGroup())), trigger.getDescription()),
                    trigger.getStartTime(),
                    trigger.getNextFireTime(),
                    trigger.getPreviousFireTime(),
                    trigger.getEndTime(),
                    trigger.getFinalFireTime(),
                    trigger.getPriority(),
                    quartzScheduler.getTriggerState(triggerKey(trigger.getKey().getName(), trigger.getKey().getGroup())).toString());
            triggerDetails.add(setExtraTriggerDetails(trigger, triggerDetail));
        }
        return triggerDetails;
    }

    public List<QuartzDetailsCO> fetchQuartzDetails() throws SchedulerException, IllegalArgumentException, NullPointerException {
        List<JobDetailsCO> jobDetails = fetchJobDetails()
                .stream()
                .sorted(((JobDetailsCO jdc1, JobDetailsCO jdc2) -> jdc1.getDetails().getKey().compareToIgnoreCase(jdc2.getDetails().getKey())))
                .collect(toList());
        List<QuartzDetailsCO> quartzDetails = new ArrayList<QuartzDetailsCO>();
        for (JobDetailsCO jobDetail : jobDetails) {
            quartzDetails.add(
                    new QuartzDetailsCO(
                            jobDetail,
                            fetchTriggerDetailsByJobKeyGroupName(new KeyGroupDescriptionDTO(jobDetail.getDetails().getKey(), jobDetail.getDetails().getGroup(), null))
                    )
            );
        }
        return quartzDetails;
    }

    public Boolean resumeTriggers(@NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, NullPointerException {
        if (isNull(keyGroupDescriptionDTO.getGroup())) {
            keyGroupDescriptionDTO.setGroup(convertFromUser(userService.getCurrentLoggedInUser()));
        }

        if (isNotEmpty(keyGroupDescriptionDTO.getKey())) {
            quartzScheduler.resumeTrigger(triggerKey(
                    keyGroupDescriptionDTO.getKey(),
                    keyGroupDescriptionDTO.getGroup().getId().toString()
            ));
            return true;
        } else {
            quartzScheduler.resumeTriggers(triggerGroupEquals(keyGroupDescriptionDTO.getGroup().getId().toString()));
            return true;
        }
    }

    public Boolean pauseTriggers(@NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        if (isNull(keyGroupDescriptionDTO.getGroup())) {
            keyGroupDescriptionDTO.setGroup(convertFromUser(userService.getCurrentLoggedInUser()));
        }

        if (isNotEmpty(keyGroupDescriptionDTO.getKey())) {
            quartzScheduler.pauseTrigger(triggerKey(
                    keyGroupDescriptionDTO.getKey(),
                    keyGroupDescriptionDTO.getGroup().getId().toString()
            ));
            return true;
        } else {
            quartzScheduler.pauseTriggers(triggerGroupEquals(keyGroupDescriptionDTO.getGroup().getId().toString()));
            return true;
        }
    }

    public Boolean resumeJobs(@NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        if (isNull(keyGroupDescriptionDTO.getGroup())) {
            keyGroupDescriptionDTO.setGroup(convertFromUser(userService.getCurrentLoggedInUser()));
        }

        if (isNotEmpty(keyGroupDescriptionDTO.getKey())) {
            quartzScheduler.resumeJob(jobKey(
                    keyGroupDescriptionDTO.getKey(),
                    keyGroupDescriptionDTO.getGroup().getId().toString()
            ));
            return true;
        } else {
            quartzScheduler.resumeJobs(jobGroupEquals(keyGroupDescriptionDTO.getGroup().getId().toString()));
            return true;
        }
    }

    public Boolean pauseJobs(@NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        if (isNull(keyGroupDescriptionDTO.getGroup())) {
            keyGroupDescriptionDTO.setGroup(convertFromUser(userService.getCurrentLoggedInUser()));
        }

        if (isNotEmpty(keyGroupDescriptionDTO.getKey())) {
            quartzScheduler.pauseJob(jobKey(
                    keyGroupDescriptionDTO.getKey(),
                    keyGroupDescriptionDTO.getGroup().getId().toString()
            ));
            return true;
        } else {
            quartzScheduler.pauseJobs(jobGroupEquals(keyGroupDescriptionDTO.getGroup().getId().toString()));
            return true;
        }
    }

    public Boolean deleteJobs(@NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        if (isNull(keyGroupDescriptionDTO.getGroup())) {
            keyGroupDescriptionDTO.setGroup(convertFromUser(userService.getCurrentLoggedInUser()));
        }

        if (isNotEmpty(keyGroupDescriptionDTO.getKey())) {
            return quartzScheduler.deleteJob(jobKey(
                    keyGroupDescriptionDTO.getKey(),
                    keyGroupDescriptionDTO.getGroup().getId().toString()
                    )
            );
        } else {
            return quartzScheduler.deleteJobs(new ArrayList<>(quartzScheduler.getJobKeys(jobGroupEquals(keyGroupDescriptionDTO.getGroup().getId().toString())
            )));

        }
    }


    public boolean deleteTriggers(@NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        if (isNull(keyGroupDescriptionDTO.getGroup())) {
            keyGroupDescriptionDTO.setGroup(convertFromUser(userService.getCurrentLoggedInUser()));
        }

        if (isNotEmpty(keyGroupDescriptionDTO.getKey())) {
            return quartzScheduler.unscheduleJob(triggerKey(
                    keyGroupDescriptionDTO.getKey(),
                    keyGroupDescriptionDTO.getGroup().getId().toString()
                    )
            );
        } else {
            return quartzScheduler.unscheduleJobs(new ArrayList<>(quartzScheduler.getTriggerKeys(triggerGroupEquals(keyGroupDescriptionDTO.getGroup().getId().toString())
            )));
        }
    }

    private Integer countTriggersOfJob(final JobKey jobKey) throws SchedulerException, IllegalArgumentException {
        List<? extends Trigger> triggersOfAJob = quartzScheduler.getTriggersOfJob(jobKey);
        return isEmpty(triggersOfAJob) ? 0 : triggersOfAJob.size();
    }

    private Map<String, String> createJobDataMap(@NotBlank @NotEmpty @NotNull final APIJobDataCO apiJobDataCO) {
        Map<String, String> jobData = new HashMap<String, String>();
        apiJobDataCO.getRequestHeaders().stream().filter(Objects::nonNull).forEach((APIHeaderCO it) -> jobData.put(format("%s_%s", REQUEST_HEADER, it.getKey()), it.getValue()));
        jobData.put(REQUEST_URL, apiJobDataCO.getRequestUrl());
        jobData.put(REQUEST_TYPE, apiJobDataCO.getRequestType().toString());
        return jobData;
    }

    private Map<String, String> createJobDataMap(@NotBlank @NotEmpty @NotNull ShellScriptJobDataCO shellScriptJobDataCO) {
        Map<String, String> jobData = new HashMap<String, String>();
        //TODO: do shell script job data map formation.
        return jobData;
    }

    private Map<String, String> createJobDataMap(@NotBlank @NotEmpty @NotNull JobCO jobCO) throws InvalidRequestException {
        Map<String, String> jobDataMap;
        switch (jobCO.getType()) {
            case API:
                jobDataMap = createJobDataMap(jobCO.getApiJobData());
                break;
            case SHELL_SCRIPT:
                jobDataMap = createJobDataMap(jobCO.getShellScriptJobData());
                break;
            default:
                throw new InvalidRequestException("");
        }
        return jobDataMap;
    }

    private JobDetail createJobDetail(final JobCO job) throws ClassNotFoundException {
        return newJob((Class<? extends Job>) forName(job.getExecutorClass().getPackageName()))
                .requestRecovery(job.getRecover())
                .storeDurably(job.getDurability())
                .withIdentity(
                        jobKey(
                                job.getDetails().getKey(),
                                nonNull(job.getDetails().getGroup()) && isNotEmpty(job.getDetails().getGroup().getId().toString()) ?
                                        job.getDetails().getGroup().getId().toString() :
                                        userService.getCurrentLoggedInUserId().toString()
                        )
                )
                .withDescription(job.getDetails().getDescription())
                .setJobData(new JobDataMap(createJobDataMap(job)))
                .build();
    }

    public JobDetailsCO fetchJobDetailsByJobKeyName(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        if(isNull(keyGroupDescriptionDTO.getGroup())) {
            keyGroupDescriptionDTO.setGroup(convertFromUser(userService.getCurrentLoggedInUser()));
        }

        final JobKey jobKey = jobKey(keyGroupDescriptionDTO.getKey(), keyGroupDescriptionDTO.getGroup().getId().toString());
        final JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        return new JobDetailsCO(
                new KeyGroupDescriptionDTO(jobDetail.getKey().getName(), convertFromUser(userService.findById(jobDetail.getKey().getGroup())), jobDetail.getDescription()),
                findJobExecutorClassByValue(jobDetail.getJobClass()),
                jobDetail.getJobDataMap().getWrappedMap(),
                jobDetail.isDurable(),
                countTriggersOfJob(jobKey) == 0,
                jobDetail.requestsRecovery(),
                jobDetail.isConcurrentExectionDisallowed(),
                jobDetail.isConcurrentExectionDisallowed()
        );
    }

    public TriggerDetailsCO fetchTriggerDetailsByTriggerKeyGroupName(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        if(isNull(keyGroupDescriptionDTO.getGroup())) {
            keyGroupDescriptionDTO.setGroup(convertFromUser(userService.getCurrentLoggedInUser()));
        }

        final Trigger trigger = quartzScheduler.getTrigger(triggerKey(keyGroupDescriptionDTO.getKey(), keyGroupDescriptionDTO.getGroup().getId().toString()));
        TriggerDetailsCO triggerDetail = new TriggerDetailsCO(
                new KeyGroupDescriptionDTO(trigger.getKey().getName(), convertFromUser(userService.findById(trigger.getKey().getGroup())), trigger.getDescription()),
                trigger.getStartTime(),
                trigger.getNextFireTime(),
                trigger.getPreviousFireTime(),
                trigger.getEndTime(),
                trigger.getFinalFireTime(),
                trigger.getPriority(),
                quartzScheduler.getTriggerState(triggerKey(trigger.getKey().getName(), trigger.getKey().getGroup())).toString());
        return setExtraTriggerDetails(trigger, triggerDetail);
    }

    private TriggerDetailsCO setExtraTriggerDetails(Trigger trigger, TriggerDetailsCO triggerDetail) {
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
        return triggerDetail;
    }

    public List<KeyGroupDescriptionDTO> jobKeysAutocomplete(@NotBlank @NotEmpty @NotNull final String searchText) throws SchedulerException {
        return quartzScheduler.getJobKeys(jobGroupEquals(userService.getCurrentLoggedInUserId().toString()))
                .stream()
                .filter(Objects::nonNull)
                .map(jobKey -> {
                    try {
                        return jobKey.getName().contains(searchText) ?
                                new KeyGroupDescriptionDTO(jobKey.getName(), convertFromUser(userService.findById(jobKey.getGroup())), quartzScheduler.getJobDetail(jobKey).getDescription()) :
                                null;
                    } catch (SchedulerException e) {
                        printRootCauseStackTrace(e);
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .collect(toList());
    }

    private TriggerBuilder getTriggerBuilder(@NotEmpty @NotNull @NotBlank final TriggerCO triggerCO) {
        TriggerBuilder triggerBuilder;
        switch (triggerCO.getScheduleType()) {
            case CRON:
                log.info("Creating CRON Trigger.");
                triggerBuilder = getBasicTriggerBuilder(triggerCO).withSchedule(getTriggerSchedulerBuilder(triggerCO.getCronTrigger()));
                break;
            case SIMPLE:
                log.info("Creating SIMPLE Trigger");
                triggerBuilder = getBasicTriggerBuilder(triggerCO).withSchedule(getTriggerSchedulerBuilder(triggerCO.getSimpleTrigger()));
                break;
            default:
                throw new InvalidRequestException("");
        }
        return triggerBuilder;
    }

    private TriggerBuilder getBasicTriggerBuilder(@NotEmpty @NotNull @NotBlank final TriggerCO triggerCO) {
        TriggerBuilder triggerBuilder = newTrigger()
                .withIdentity(
                        triggerKey(
                                triggerCO.getDetails().getKey(),
                                nonNull(triggerCO.getDetails().getGroup()) && isNotEmpty(triggerCO.getDetails().getGroup().getId().toString()) ?
                                        triggerCO.getDetails().getGroup().getId().toString() :
                                        userService.getCurrentLoggedInUserId().toString()
                        )
                )
                .withDescription(triggerCO.getDetails().getDescription());

        if (isTrue(triggerCO.getStartNow())) {
            triggerBuilder = triggerBuilder.startNow();
        } else {
            if (nonNull(triggerCO.getStartTime())) {
                triggerBuilder = triggerBuilder.startAt(triggerCO.getStartTime());
            } else {
                throw new InvalidRequestException("");
            }
        }
        if (nonNull(triggerCO.getEndTime())) {
            triggerBuilder = triggerBuilder.endAt(triggerCO.getEndTime());
        }
        return triggerBuilder;
    }

    private CronScheduleBuilder getTriggerSchedulerBuilder(@NotEmpty @NotNull @NotBlank final CronJobSchedulerDataCO cronJobSchedulerDataCO) {
        return cronSchedule(cronJobSchedulerDataCO.getCronExpression()).inTimeZone(getDefault());
    }

    private SimpleScheduleBuilder getTriggerSchedulerBuilder(@NotEmpty @NotNull @NotBlank final SimpleJobSchedulerDataCO simpleJobSchedulerDataCO) {
        SimpleScheduleBuilder simpleScheduleBuilder;
        switch (simpleJobSchedulerDataCO.getRepeatType()) {
            case REPEAT_BY_SECOND:
                if (simpleJobSchedulerDataCO.getRepeatInterval().getRepeatForever()) {
                    simpleScheduleBuilder = repeatSecondlyForever(simpleJobSchedulerDataCO.getRepeatInterval().getRepeatValue());
                } else {
                    if (simpleJobSchedulerDataCO.getRepeatInterval().getRepeatCount() > 0) {
                        simpleScheduleBuilder = repeatSecondlyForTotalCount(simpleJobSchedulerDataCO.getRepeatInterval().getRepeatCount(), simpleJobSchedulerDataCO.getRepeatInterval().getRepeatValue());
                    } else {
                        throw new InvalidRequestException("");
                    }
                }
                break;
            case REPEAT_BY_MINUTE:
                if (simpleJobSchedulerDataCO.getRepeatInterval().getRepeatForever()) {
                    simpleScheduleBuilder = repeatMinutelyForever(simpleJobSchedulerDataCO.getRepeatInterval().getRepeatValue());
                } else {
                    if (simpleJobSchedulerDataCO.getRepeatInterval().getRepeatCount() > 0) {
                        simpleScheduleBuilder = repeatMinutelyForTotalCount(simpleJobSchedulerDataCO.getRepeatInterval().getRepeatCount(), simpleJobSchedulerDataCO.getRepeatInterval().getRepeatValue());
                    } else {
                        throw new InvalidRequestException("");
                    }
                }
                break;
            case REPEAT_BY_HOUR:
                if (simpleJobSchedulerDataCO.getRepeatInterval().getRepeatForever()) {
                    simpleScheduleBuilder = repeatHourlyForever(simpleJobSchedulerDataCO.getRepeatInterval().getRepeatValue());
                } else {
                    if (simpleJobSchedulerDataCO.getRepeatInterval().getRepeatCount() > 0) {
                        simpleScheduleBuilder = repeatHourlyForTotalCount(simpleJobSchedulerDataCO.getRepeatInterval().getRepeatCount(), simpleJobSchedulerDataCO.getRepeatInterval().getRepeatValue());
                    } else {
                        throw new InvalidRequestException("");
                    }
                }
                break;
            default:
                throw new InvalidRequestException("");
        }
        return simpleScheduleBuilder;
    }

    public boolean jobKeyExists(String keyName) throws SchedulerException {
        return quartzScheduler.checkExists(
                jobKey(
                        keyName,
                        userService.getCurrentLoggedInUserId().toString()
                )
        );
    }

    public boolean triggerKeyExists(String keyName) throws SchedulerException {
        return quartzScheduler.checkExists(
                triggerKey(
                        keyName,
                        userService.getCurrentLoggedInUserId().toString()
                )
        );
    }
}
