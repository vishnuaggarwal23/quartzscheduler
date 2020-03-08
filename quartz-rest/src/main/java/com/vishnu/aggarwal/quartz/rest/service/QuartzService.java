package com.vishnu.aggarwal.quartz.rest.service;

import com.vishnu.aggarwal.quartz.core.co.*;
import com.vishnu.aggarwal.quartz.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.quartz.core.dto.QuartzDTO;
import com.vishnu.aggarwal.quartz.core.dto.UserDTO;
import com.vishnu.aggarwal.quartz.core.enums.JobExecutorClass;
import com.vishnu.aggarwal.quartz.core.exceptions.InvalidRequestException;
import com.vishnu.aggarwal.quartz.core.service.BaseService;
import com.vishnu.aggarwal.quartz.rest.interfaces.UserService;
import lombok.NonNull;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Caching;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

import static com.vishnu.aggarwal.quartz.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.quartz.core.enums.JobExecutorClass.findJobExecutorClassByValue;
import static com.vishnu.aggarwal.quartz.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.quartz.core.enums.ScheduleType.SIMPLE;
import static com.vishnu.aggarwal.quartz.rest.util.DTOConversion.convertFromUser;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Class.forName;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.TimeZone.getDefault;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.apache.commons.lang3.exception.ExceptionUtils.getRootCauseMessage;
import static org.quartz.CronScheduleBuilder.cronSchedule;
import static org.quartz.JobBuilder.newJob;
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
     * @param userServiceImpl the user service
     */
    @Autowired
    public QuartzService(
            @NonNull final Scheduler quartzScheduler,
            @NonNull final UserServiceImpl userServiceImpl) {
        this.quartzScheduler = quartzScheduler;
        this.userService = userServiceImpl;
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
    @CacheEvict(value = "isUniqueJobKey", allEntries = true, beforeInvocation = true)
    public void createNewUnscheduledApiJob(@NonNull final QuartzDTO quartzDTO) throws SchedulerException, NullPointerException, ClassNotFoundException, IllegalArgumentException {
        quartzScheduler.addJob(
                createJobDetail(
                        quartzDTO.getApiJobData(),
                        quartzDTO.getJob()
                ),
                false,
                quartzDTO.getJob().getDurability() ? FALSE : TRUE
        );
    }

    /**
     * Create new scheduled api simple job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws ClassNotFoundException  the class not found exception
     * @throws SchedulerException      the scheduler exception
     * @throws InvalidRequestException the invalid request exception
     */
    @Caching(
            evict = {
                    @CacheEvict(value = "isUniqueJobKey", allEntries = true, beforeInvocation = true),
                    @CacheEvict(value = "isUniqueTriggerKey", allEntries = true, beforeInvocation = true)
            }
    )
    public @NonNull Date createNewScheduledApiSimpleJob(@NonNull final QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, InvalidRequestException {
        @NonNull final JobDetail jobDetail = createJobDetail(quartzDTO.getApiJobData(), quartzDTO.getJob());
        return quartzScheduler.scheduleJob(
                jobDetail,
                getSimpleTriggerBuilder(
                        quartzDTO.getApiJobData().getSimpleJobScheduler(),
                        jobDetail
                ).build()
        );
    }

    /**
     * Create new scheduled api cron job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws ClassNotFoundException  the class not found exception
     * @throws SchedulerException      the scheduler exception
     * @throws InvalidRequestException the invalid request exception
     */
    @Caching(
            evict = {
                    @CacheEvict(value = "isUniqueJobKey", allEntries = true, beforeInvocation = true),
                    @CacheEvict(value = "isUniqueTriggerKey", allEntries = true, beforeInvocation = true)
            }
    )
    public @NonNull Date createNewScheduledApiCronJob(@NonNull final QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, InvalidRequestException {
        @NonNull final JobDetail jobDetail = createJobDetail(quartzDTO.getApiJobData(), quartzDTO.getJob());
        return quartzScheduler.scheduleJob(
                jobDetail,
                getCronTriggerBuilder(
                        quartzDTO.getApiJobData().getCronJobScheduler(),
                        new TriggerKey(
                                quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getKey(),
                                userService.getCurrentLoggedInUser().getId().toString()
                        ),
                        jobDetail
                ).build()
        );
    }

    /**
     * Create new simple trigger for job.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     * @throws IllegalStateException    the illegal state exception
     */
    @CacheEvict(value = "isUniqueTriggerKey", allEntries = true, beforeInvocation = true)
    public @NonNull Date createNewSimpleTriggerForJob(@NonNull final QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException, IllegalStateException {
        return quartzScheduler.scheduleJob(
                getSimpleTriggerBuilder(
                        quartzDTO.getApiJobData().getSimpleJobScheduler(),
                        quartzScheduler.getJobDetail(
                                new JobKey(
                                        quartzDTO.getJob().getDetails().getKey(),
                                        userService.getCurrentLoggedInUser().getId().toString()
                                )
                        )
                ).build()
        );
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
    @CacheEvict(value = "isUniqueTriggerKey", allEntries = true, beforeInvocation = true)
    public @NonNull Date createNewCronTriggerForJob(@NonNull final QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        return quartzScheduler.scheduleJob(
                getCronTriggerBuilder(
                        quartzDTO.getApiJobData().getCronJobScheduler(),
                        new TriggerKey(
                                quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getKey(),
                                userService.getCurrentLoggedInUser().getId().toString()
                        ),
                        quartzScheduler.getJobDetail(new JobKey(
                                quartzDTO.getJob().getDetails().getKey(),
                                userService.getCurrentLoggedInUser().getId().toString()
                        ))).build()
        );
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
    @CacheEvict(value = "isUniqueJobKey", allEntries = true, beforeInvocation = true)
    public void updateExistingJob(@NonNull final QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, IllegalArgumentException, NullPointerException {
        quartzScheduler.addJob(
                createJobDetail(
                        quartzDTO.getApiJobData(),
                        quartzDTO.getJob()
                ),
                true,
                quartzDTO.getJob().getDurability() ? FALSE : TRUE
        );
    }

    /**
     * Update existing simple trigger date.
     *
     * @param quartzDTO the quartz dto
     * @return the date
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     * @throws IllegalStateException    the illegal state exception
     */
    @CacheEvict(value = "isUniqueTriggerKey", allEntries = true, beforeInvocation = true)
    public @NonNull Date updateExistingSimpleTrigger(@NonNull final QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException, IllegalStateException {
        return quartzScheduler.scheduleJob(
                getSimpleTriggerBuilder(
                        quartzDTO.getApiJobData().getSimpleJobScheduler(),
                        quartzScheduler.getJobDetail(
                                new JobKey(
                                        quartzDTO.getJob().getDetails().getKey(),
                                        userService.getCurrentLoggedInUser().getId().toString()
                                )
                        )
                ).build()
        );
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
    @CacheEvict(value = "isUniqueTriggerKey", allEntries = true, beforeInvocation = true)
    public @NonNull Date updateExistingCronTrigger(@NonNull final QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        return quartzScheduler.scheduleJob(
                getCronTriggerBuilder(
                        quartzDTO.getApiJobData().getCronJobScheduler(),
                        new TriggerKey(
                                quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getKey(),
                                userService.getCurrentLoggedInUser().getId().toString()
                        ),
                        quartzScheduler.getJobDetail(
                                new JobKey(
                                        quartzDTO.getJob().getDetails().getKey(),
                                        userService.getCurrentLoggedInUser().getId().toString()
                                )
                        )
                ).build()
        );
    }

    /**
     * Fetch job details by group name list.
     *
     * @return the list
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public List<JobDetailsCO> fetchJobDetails() throws SchedulerException, IllegalArgumentException, NullPointerException {
        @NonNull final UserDTO userDTO = convertFromUser(userService.getCurrentLoggedInUser());
        /*List<JobDetailsCO> jobDetails = new ArrayList<JobDetailsCO>();
        for (JobKey jobKey : quartzScheduler.getJobKeys(jobGroupEquals(userDTO.getId().toString())).parallelStream().sorted(((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))).collect(toCollection(LinkedHashSet::new))) {
            JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
            jobDetails.add(new JobDetailsCO(
                    new KeyGroupDescriptionDTO(jobKey.getName(), userDTO, jobDetail.getDescription()),
                    findJobExecutorClassByValue(jobDetail.getJobClass()),
                    createMapFromJobDataMap(jobDetail.getJobDataMap()),
                    jobDetail.isDurable(),
                    countTriggersOfJob(jobKey) == 0,
                    jobDetail.requestsRecovery(),
                    jobDetail.isConcurrentExectionDisallowed(),
                    jobDetail.isPersistJobDataAfterExecution(),
                    null));
        }*/

        return quartzScheduler.getJobKeys(jobGroupEquals(userDTO.getId().toString()))
                .parallelStream()
                .sorted(((JobKey o1, JobKey o2) -> o1.getName().compareToIgnoreCase(o2.getName())))
                .map((JobKey jobKey) -> {
                    JobDetailsCO jobDetailsCO = null;
                    try {
                        @NonNull final JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
                        jobDetailsCO = new JobDetailsCO(
                                new KeyGroupDescriptionDTO(jobKey.getName(), userDTO, jobDetail.getDescription()),
                                findJobExecutorClassByValue(jobDetail.getJobClass()),
                                createMapFromJobDataMap(jobDetail.getJobDataMap()),
                                jobDetail.isDurable(),
                                countTriggersOfJob(jobKey) == 0,
                                jobDetail.requestsRecovery(),
                                jobDetail.isConcurrentExectionDisallowed(),
                                jobDetail.isPersistJobDataAfterExecution(),
                                null);
                    } catch (SchedulerException e) {
                        log.error(getRootCauseMessage(e));
                    }
                    return jobDetailsCO;
                }).collect(toList());

//        return jobDetails;
    }

    /**
     * Fetch trigger details by job key name and group name list.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the list
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    @SuppressWarnings("unchecked")
    public List<TriggerDetailsCO> fetchTriggerDetailsByJobKeyName(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        @NonNull final UserDTO userDTO = convertFromUser(userService.getCurrentLoggedInUser());
//        List<TriggerDetailsCO> triggerDetails = new ArrayList<TriggerDetailsCO>();

        return quartzScheduler.getTriggersOfJob(new JobKey(keyGroupDescriptionDTO.getKey(), userDTO.getId().toString()))
                .parallelStream()
                .sorted((o1, o2) -> o1.getKey().getName().compareToIgnoreCase(o2.getKey().getName()))
                .map((Trigger trigger) -> {
                    TriggerDetailsCO triggerDetail = null;
                    try {
                        triggerDetail = setExtraTriggerDetails(trigger, new TriggerDetailsCO(
                                new KeyGroupDescriptionDTO(trigger.getKey().getName(), userDTO, trigger.getDescription()),
                                trigger.getStartTime(),
                                trigger.getNextFireTime(),
                                trigger.getPreviousFireTime(),
                                trigger.getEndTime(),
                                trigger.getFinalFireTime(),
                                trigger.getPriority(),
                                quartzScheduler.getTriggerState(triggerKey(trigger.getKey().getName(), trigger.getKey().getGroup())).toString()));
                    } catch (SchedulerException e) {
                        log.error(getRootCauseMessage(e));
                    }
                    return triggerDetail;
                })
                .collect(toList());


        /*for (Trigger trigger : quartzScheduler.getTriggersOfJob(new JobKey(keyGroupDescriptionDTO.getKey(), userDTO.getId().toString())).stream().sorted((o1, o2) -> o1.getKey().getName().compareToIgnoreCase(o2.getKey().getName())).collect(toList())) {
            TriggerDetailsCO triggerDetail = new TriggerDetailsCO(
                    new KeyGroupDescriptionDTO(trigger.getKey().getName(), userDTO, trigger.getDescription()),
                    trigger.getStartTime(),
                    trigger.getNextFireTime(),
                    trigger.getPreviousFireTime(),
                    trigger.getEndTime(),
                    trigger.getFinalFireTime(),
                    trigger.getPriority(),
                    quartzScheduler.getTriggerState(triggerKey(trigger.getKey().getName(), trigger.getKey().getGroup())).toString());
            triggerDetails.add(setExtraTriggerDetails(trigger, triggerDetail));
        }
        return triggerDetails;*/
    }

    /**
     * Fetch quartz details for a group name list.
     *
     * @return the list
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public List<QuartzDetailsCO> fetchQuartzDetails() throws SchedulerException, IllegalArgumentException, NullPointerException {
        /*List<JobDetailsCO> jobDetails = fetchJobDetails()
                .parallelStream()
                .sorted(((o1, o2) -> o1.getDetails().getKey().compareToIgnoreCase(o2.getDetails().getKey())))
                .collect(toList());*/

        return fetchJobDetails()
                .parallelStream()
                .sorted(((JobDetailsCO o1, JobDetailsCO o2) -> o1.getDetails().getKey().compareToIgnoreCase(o2.getDetails().getKey())))
                .map((JobDetailsCO jobDetailsCo) -> {
                    QuartzDetailsCO quartzDetailsCO = null;
                    try {
                        quartzDetailsCO = new QuartzDetailsCO(jobDetailsCo, fetchTriggerDetailsByJobKeyName(new KeyGroupDescriptionDTO(jobDetailsCo.getDetails())));
                    } catch (SchedulerException e) {
                        log.error(getRootCauseMessage(e));
                    }
                    return quartzDetailsCO;
                })
                .collect(toList());

        /*List<QuartzDetailsCO> quartzDetails = new ArrayList<QuartzDetailsCO>();
        for (JobDetailsCO jobDetail : jobDetails) {
            quartzDetails.add(new QuartzDetailsCO(jobDetail, fetchTriggerDetailsByJobKeyName(new KeyGroupDescriptionDTO(jobDetail.getDetails().getKey(), jobDetail.getDetails().getGroup(), null))));
        }
        return quartzDetails;*/
    }

    /**
     * Resume triggers.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the boolean
     * @throws SchedulerException   the scheduler exception
     * @throws NullPointerException the null pointer exception
     */
    public @NonNull
    final boolean resumeTriggers(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, NullPointerException {
        if (isNotEmpty(keyGroupDescriptionDTO.getKey())) {
            quartzScheduler.resumeTrigger(new TriggerKey(keyGroupDescriptionDTO.getKey(), userService.getCurrentLoggedInUser().getId().toString()));
        } else {
            quartzScheduler.resumeTriggers(triggerGroupEquals(userService.getCurrentLoggedInUser().getId().toString()));
        }
        return true;
    }

    /**
     * Pause triggers.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the boolean
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public @NonNull
    final boolean pauseTriggers(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        if (isNotEmpty(keyGroupDescriptionDTO.getKey())) {
            quartzScheduler.pauseTrigger(new TriggerKey(keyGroupDescriptionDTO.getKey(), userService.getCurrentLoggedInUser().getId().toString()));
        } else {
            quartzScheduler.pauseTriggers(triggerGroupEquals(userService.getCurrentLoggedInUser().getId().toString()));
        }
        return true;
    }

    /**
     * Resume jobs.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the boolean
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public @NonNull
    final boolean resumeJobs(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        if (isNotEmpty(keyGroupDescriptionDTO.getKey())) {
            quartzScheduler.resumeJob(new JobKey(keyGroupDescriptionDTO.getKey(), userService.getCurrentLoggedInUser().getId().toString()));
        } else {
            quartzScheduler.resumeJobs(jobGroupEquals(userService.getCurrentLoggedInUser().getId().toString()));
        }
        return true;
    }

    /**
     * Pause jobs.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the boolean
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public @NonNull
    final boolean pauseJobs(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        if (isNotEmpty(keyGroupDescriptionDTO.getKey())) {
            quartzScheduler.pauseJob(new JobKey(keyGroupDescriptionDTO.getKey(), userService.getCurrentLoggedInUser().getId().toString()));
        } else {
            quartzScheduler.pauseJobs(jobGroupEquals(userService.getCurrentLoggedInUser().getId().toString()));
        }
        return true;
    }

    /**
     * Delete jobs boolean.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the boolean
     * @throws SchedulerException the scheduler exception
     */
    @Caching(
            evict = {
                    @CacheEvict(value = "isUniqueJobKey", allEntries = true, beforeInvocation = true),
                    @CacheEvict(value = "isUniqueTriggerKey", allEntries = true, beforeInvocation = true)
            }
    )
    public @NonNull boolean deleteJobs(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        return isNotEmpty(keyGroupDescriptionDTO.getKey()) ? quartzScheduler.deleteJob(new JobKey(keyGroupDescriptionDTO.getKey(), userService.getCurrentLoggedInUser().getId().toString())) : quartzScheduler.deleteJobs(new ArrayList<>(quartzScheduler.getJobKeys(jobGroupEquals(userService.getCurrentLoggedInUser().getId().toString()))));
    }

    /**
     * Delete triggers boolean.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the boolean
     * @throws SchedulerException the scheduler exception
     */
    @CacheEvict(value = "isUniqueTriggerKey", allEntries = true, beforeInvocation = true)
    public @NonNull boolean deleteTriggers(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        return isNotEmpty(keyGroupDescriptionDTO.getKey()) ? quartzScheduler.unscheduleJob(new TriggerKey(keyGroupDescriptionDTO.getKey(), userService.getCurrentLoggedInUser().getId().toString())) : quartzScheduler.unscheduleJobs(new ArrayList<>(quartzScheduler.getTriggerKeys(triggerGroupEquals(userService.getCurrentLoggedInUser().getId().toString()))));
    }

    private @NonNull int countTriggersOfJob(@NonNull final JobKey jobKey) throws SchedulerException, IllegalArgumentException {
        final List<? extends Trigger> triggersOfAJob = quartzScheduler.getTriggersOfJob(jobKey);
        return isEmpty(triggersOfAJob) ? 0 : triggersOfAJob.size();
    }

    private @NonNull Map<String, String> createJobDataMap(@NonNull @NotBlank @NotEmpty final List<APIHeaderCO> requestHeaders, @NonNull @NotBlank final String requestUrl, @NonNull final HttpMethod requestType) {
        Map<String, String> jobData = new HashMap<String, String>();
        requestHeaders.stream().filter(Objects::nonNull).forEach((APIHeaderCO it) -> jobData.put(format("%s_%s", REQUEST_HEADER, it.getKey()), it.getValue()));
        jobData.put(REQUEST_URL, requestUrl);
        jobData.put(REQUEST_TYPE, requestType.toString());
        return jobData;
    }

    private @NonNull Map<String, Object> createMapFromJobDataMap(@NonNull @NotEmpty final JobDataMap jobDataMap) throws IllegalArgumentException {
        Map<String, Object> map = new HashMap<String, Object>();
        jobDataMap.forEach(map::put);
        return map;
    }

    private @NonNull Class<? extends Job> getExecutorClass(@NonNull final JobExecutorClass jobExecutorClass) throws ClassNotFoundException {
        return (Class<? extends Job>) forName(jobExecutorClass.getPackageName());
    }

    private @NonNull JobDetail createJobDetail(@NonNull final APIJobDataCO apiJobData, @NonNull final JobCO job) throws ClassNotFoundException {
        return newJob(getExecutorClass(apiJobData.getExecutorClass()))
                .requestRecovery(job.getRecover())
                .storeDurably(job.getDurability())
                .withIdentity(new JobKey(job.getDetails().getKey(), userService.getCurrentLoggedInUser().getId().toString()))
                .withDescription(job.getDetails().getDescription())
                .setJobData(new JobDataMap(createJobDataMap(apiJobData.getRequestHeaders(), apiJobData.getRequestUrl(), apiJobData.getRequestType())))
                .build();
    }

    private @NonNull TriggerBuilder getCronTriggerBuilder(@NonNull final CronJobSchedulerDataCO cronJobSchedulerData, @NonNull final TriggerKey triggerKey, @NonNull final JobDetail jobDetail) {
        return getTriggerBuilder(newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(cronJobSchedulerData.getTrigger().getDetails().getDescription())
                .withIdentity(triggerKey)
                .withSchedule(cronSchedule(cronJobSchedulerData.getCronExpression()).inTimeZone(getDefault())), cronJobSchedulerData.getTrigger());
    }

    private @NonNull TriggerBuilder getTriggerBuilder(@NonNull TriggerBuilder triggerBuilder, @NonNull final TriggerCO trigger) throws InvalidRequestException {
        if (isTrue(trigger.getStartNow())) {
            triggerBuilder = triggerBuilder.startNow();
        } else {
            if (nonNull(trigger.getStartTime())) {
                triggerBuilder = triggerBuilder.startAt(trigger.getStartTime());
            } else {
                throw new InvalidRequestException("");
            }
        }
        if (nonNull(trigger.getEndTime())) {
            triggerBuilder = triggerBuilder.endAt(trigger.getEndTime());
        }
        return triggerBuilder;
    }

    private @NonNull TriggerBuilder getSimpleTriggerBuilder(@NonNull final SimpleJobSchedulerDataCO simpleJobSchedulerData, @NonNull final JobDetail jobDetail) throws InvalidRequestException {

        @NonNull TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(simpleJobSchedulerData.getTrigger().getDetails().getDescription())
                .withIdentity(new TriggerKey(simpleJobSchedulerData.getTrigger().getDetails().getKey(), userService.getCurrentLoggedInUser().getId().toString()));

        switch (simpleJobSchedulerData.getRepeatType()) {
            case REPEAT_BY_SECOND:
                if (simpleJobSchedulerData.getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(repeatSecondlyForever(simpleJobSchedulerData.getRepeatInterval().getRepeatValue()));
                } else {
                    if (simpleJobSchedulerData.getRepeatInterval().getRepeatCount() > 0) {
                        triggerBuilder = triggerBuilder.withSchedule(repeatSecondlyForTotalCount(simpleJobSchedulerData.getRepeatInterval().getRepeatCount(), simpleJobSchedulerData.getRepeatInterval().getRepeatValue()));
                    } else {
                        throw new InvalidRequestException("");
                    }
                }
                break;
            case REPEAT_BY_MINUTE:
                if (simpleJobSchedulerData.getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(repeatMinutelyForever(simpleJobSchedulerData.getRepeatInterval().getRepeatValue()));
                } else {
                    if (simpleJobSchedulerData.getRepeatInterval().getRepeatCount() > 0) {
                        triggerBuilder = triggerBuilder.withSchedule(repeatMinutelyForTotalCount(simpleJobSchedulerData.getRepeatInterval().getRepeatCount(), simpleJobSchedulerData.getRepeatInterval().getRepeatValue()));
                    } else {
                        throw new InvalidRequestException("");
                    }
                }
                break;
            case REPEAT_BY_HOUR:
                if (simpleJobSchedulerData.getRepeatInterval().getRepeatForever()) {
                    triggerBuilder = triggerBuilder.withSchedule(repeatHourlyForever(simpleJobSchedulerData.getRepeatInterval().getRepeatValue()));
                } else {
                    if (simpleJobSchedulerData.getRepeatInterval().getRepeatCount() > 0) {
                        triggerBuilder = triggerBuilder.withSchedule(repeatHourlyForTotalCount(simpleJobSchedulerData.getRepeatInterval().getRepeatCount(), simpleJobSchedulerData.getRepeatInterval().getRepeatValue()));
                    } else {
                        throw new InvalidRequestException("");
                    }
                }
                break;
            default:
                throw new InvalidRequestException("");
        }
        return getTriggerBuilder(triggerBuilder, simpleJobSchedulerData.getTrigger());
    }

    /**
     * Fetch job details by job key name and job group name job details co.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the job details co
     * @throws SchedulerException the scheduler exception
     */
    public @NonNull
    final JobDetailsCO fetchJobDetailsByJobKeyName(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        @NonNull final UserDTO userDTO = convertFromUser(userService.getCurrentLoggedInUser());
        @NonNull final JobKey jobKey = new JobKey(keyGroupDescriptionDTO.getKey(), userDTO.getId().toString());
        @NonNull final JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        return new JobDetailsCO(
                new KeyGroupDescriptionDTO(jobKey.getName(), userDTO, jobDetail.getDescription()),
                findJobExecutorClassByValue(jobDetail.getJobClass()),
                createMapFromJobDataMap(jobDetail.getJobDataMap()),
                jobDetail.isDurable(),
                countTriggersOfJob(jobKey) == 0,
                jobDetail.requestsRecovery(),
                jobDetail.isConcurrentExectionDisallowed(),
                jobDetail.isConcurrentExectionDisallowed(),
                null);
    }

    /**
     * Fetch trigger details by trigger key name and trigger group name trigger details co.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the trigger details co
     * @throws SchedulerException the scheduler exception
     */
    public @NonNull
    final TriggerDetailsCO fetchTriggerDetailsByTriggerKeyName(@NonNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        @NonNull final UserDTO userDTO = convertFromUser(userService.getCurrentLoggedInUser());
        @NonNull final Trigger trigger = quartzScheduler.getTrigger(new TriggerKey(keyGroupDescriptionDTO.getKey(), userDTO.getId().toString()));
        return setExtraTriggerDetails(trigger, new TriggerDetailsCO(
                new KeyGroupDescriptionDTO(trigger.getKey().getName(), userDTO, trigger.getDescription()),
                trigger.getStartTime(),
                trigger.getNextFireTime(),
                trigger.getPreviousFireTime(),
                trigger.getEndTime(),
                trigger.getFinalFireTime(),
                trigger.getPriority(),
                quartzScheduler.getTriggerState(triggerKey(trigger.getKey().getName(), trigger.getKey().getGroup())).toString()));
    }

    private @NonNull TriggerDetailsCO setExtraTriggerDetails(@NonNull final Trigger trigger, TriggerDetailsCO triggerDetail) {
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

    public final List<KeyGroupDescriptionDTO> jobKeysAutocomplete(@NonNull final String searchText) throws SchedulerException {
        return quartzScheduler.getJobKeys(jobGroupEquals(userService.getCurrentLoggedInUser().getId().toString()))
                .parallelStream()
                .filter(Objects::nonNull)
                .map(jobKey -> {
                    KeyGroupDescriptionDTO keyGroupDescriptionDTO = null;
                    try {
                        keyGroupDescriptionDTO = jobKey.getName().contains(searchText) ? new KeyGroupDescriptionDTO(jobKey.getName(), convertFromUser(userService.findById(jobKey.getGroup())), quartzScheduler.getJobDetail(jobKey).getDescription()) : null;
                    } catch (SchedulerException e) {
                        log.error(ExceptionUtils.getRootCauseMessage(e));
                    }
                    return keyGroupDescriptionDTO;
                })
                .collect(toList())
                .parallelStream()
                .filter(Objects::nonNull)
                .collect(toList());
    }
}
