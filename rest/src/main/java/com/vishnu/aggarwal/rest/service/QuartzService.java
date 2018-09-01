package com.vishnu.aggarwal.rest.service;

import com.vishnu.aggarwal.core.co.*;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.enums.JobExecutorClass;
import com.vishnu.aggarwal.core.exceptions.InvalidRequestException;
import com.vishnu.aggarwal.core.service.BaseService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import java.util.*;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.enums.JobExecutorClass.findJobExecutorClassByValue;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.Class.forName;
import static java.lang.String.format;
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

    /**
     * Instantiates a new Quartz service.
     *
     * @param quartzScheduler the quartz scheduler
     */
    @Autowired
    public QuartzService(Scheduler quartzScheduler) {
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
    public void createNewUnscheduledApiJob(final QuartzDTO quartzDTO) throws SchedulerException, NullPointerException, ClassNotFoundException, IllegalArgumentException {
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
    public Date createNewScheduledApiSimpleJob(final QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, InvalidRequestException {
        final JobDetail jobDetail = createJobDetail(quartzDTO.getApiJobData(), quartzDTO.getJob());
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
    public Date createNewScheduledApiCronJob(final QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, InvalidRequestException {
        final JobDetail jobDetail = createJobDetail(quartzDTO.getApiJobData(), quartzDTO.getJob());
        return quartzScheduler.scheduleJob(
                jobDetail,
                getCronTriggerBuilder(
                        quartzDTO.getApiJobData().getCronJobScheduler(),
                        new TriggerKey(
                                quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getKeyName(),
                                quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getGroupName()
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
    public Date createNewSimpleTriggerForJob(final QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException, IllegalStateException {
        return quartzScheduler.scheduleJob(
                getSimpleTriggerBuilder(
                        quartzDTO.getApiJobData().getSimpleJobScheduler(),
                        quartzScheduler.getJobDetail(
                                new JobKey(
                                        quartzDTO.getJob().getDetails().getKeyName(),
                                        quartzDTO.getJob().getDetails().getGroupName()
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
    public Date createNewCronTriggerForJob(final QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        return quartzScheduler.scheduleJob(
                getCronTriggerBuilder(
                        quartzDTO.getApiJobData().getCronJobScheduler(),
                        new TriggerKey(
                                quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getKeyName(),
                                quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getGroupName()
                        ),
                        quartzScheduler.getJobDetail(new JobKey(
                                quartzDTO.getJob().getDetails().getKeyName(),
                                quartzDTO.getJob().getDetails().getGroupName()
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
    public void updateExistingJob(final QuartzDTO quartzDTO) throws ClassNotFoundException, SchedulerException, IllegalArgumentException, NullPointerException {
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
    public Date updateExistingSimpleTrigger(final QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException, IllegalStateException {
        return quartzScheduler.scheduleJob(
                getSimpleTriggerBuilder(
                        quartzDTO.getApiJobData().getSimpleJobScheduler(),
                        quartzScheduler.getJobDetail(
                                new JobKey(
                                        quartzDTO.getJob().getDetails().getKeyName(),
                                        quartzDTO.getJob().getDetails().getGroupName()
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
    public Date updateExistingCronTrigger(final QuartzDTO quartzDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        return quartzScheduler.scheduleJob(
                getCronTriggerBuilder(
                        quartzDTO.getApiJobData().getCronJobScheduler(),
                        new TriggerKey(
                                quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getKeyName(),
                                quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails().getGroupName()
                        ),
                        quartzScheduler.getJobDetail(
                                new JobKey(
                                        quartzDTO.getJob().getDetails().getKeyName(),
                                        quartzDTO.getJob().getDetails().getGroupName()
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
    public @NotEmpty List<JobDetailsCO> fetchJobDetailsByJobGroupName(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        List<JobDetailsCO> jobDetails = new ArrayList<JobDetailsCO>();
        for (JobKey jobKey : quartzScheduler.getJobKeys(jobGroupEquals(keyGroupDescriptionDTO.getGroupName())).parallelStream().sorted(((o1, o2) -> o1.getName().compareToIgnoreCase(o2.getName()))).collect(toSet())) {
            JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
            jobDetails.add(new JobDetailsCO(
                    jobKey.getName(),
                    jobKey.getGroup(),
                    jobDetail.getDescription(),
                    findJobExecutorClassByValue(jobDetail.getJobClass()),
                    createMapFromJobDataMap(jobDetail.getJobDataMap()),
                    jobDetail.isDurable(),
                    countTriggersOfJob(jobKey) == 0,
                    jobDetail.requestsRecovery(),
                    jobDetail.isConcurrentExectionDisallowed(),
                    jobDetail.isConcurrentExectionDisallowed(),
                    null));
        }
        return jobDetails;
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
    public @NotEmpty List<TriggerDetailsCO> fetchTriggerDetailsByJobKeyNameAndJobGroupName(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {

        List<TriggerDetailsCO> triggerDetails = new ArrayList<TriggerDetailsCO>();
        for (Trigger trigger : quartzScheduler.getTriggersOfJob(new JobKey(keyGroupDescriptionDTO.getKeyName(), keyGroupDescriptionDTO.getGroupName())).stream().sorted((o1, o2) -> o1.getKey().getName().compareToIgnoreCase(o2.getKey().getName())).collect(toList())) {
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
            triggerDetails.add(setExtraTriggerDetails(trigger, triggerDetail));
        }
        return triggerDetails;
    }

    /**
     * Fetch quartz details for a group name list.
     *
     * @return the list
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public @NotEmpty List<QuartzDetailsCO> fetchQuartzDetailsForJobGroupName(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        List<JobDetailsCO> jobDetails = fetchJobDetailsByJobGroupName(keyGroupDescriptionDTO).stream().sorted(((o1, o2) -> o1.getKeyName().compareToIgnoreCase(o2.getKeyName()))).collect(toList());
        List<QuartzDetailsCO> quartzDetails = new ArrayList<QuartzDetailsCO>();
        for (JobDetailsCO jobDetail : jobDetails) {
            quartzDetails.add(new QuartzDetailsCO(jobDetail, fetchTriggerDetailsByJobKeyNameAndJobGroupName(new KeyGroupDescriptionDTO(jobDetail.getKeyName(), jobDetail.getGroupName(), null))));
        }
        return quartzDetails;
    }

    /**
     * Resume triggers.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Boolean resumeTriggers(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, NullPointerException {
        if (isNotEmpty(keyGroupDescriptionDTO.getKeyName())) {
            quartzScheduler.resumeTrigger(new TriggerKey(keyGroupDescriptionDTO.getKeyName(), keyGroupDescriptionDTO.getGroupName()));
            return true;
        } else {
            quartzScheduler.resumeTriggers(triggerGroupEquals(keyGroupDescriptionDTO.getGroupName()));
            return true;
        }
    }

    /**
     * Pause triggers.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Boolean pauseTriggers(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        if (isNotEmpty(keyGroupDescriptionDTO.getKeyName())) {
            quartzScheduler.pauseTrigger(new TriggerKey(keyGroupDescriptionDTO.getKeyName(), keyGroupDescriptionDTO.getGroupName()));
            return true;
        } else {
            quartzScheduler.pauseTriggers(triggerGroupEquals(keyGroupDescriptionDTO.getGroupName()));
            return true;
        }
    }

    /**
     * Resume jobs.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Boolean resumeJobs(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        if (isNotEmpty(keyGroupDescriptionDTO.getKeyName())) {
            quartzScheduler.resumeJob(new JobKey(keyGroupDescriptionDTO.getKeyName(), keyGroupDescriptionDTO.getGroupName()));
            return true;
        } else {
            quartzScheduler.resumeJobs(jobGroupEquals(keyGroupDescriptionDTO.getGroupName()));
            return true;
        }
    }

    /**
     * Pause jobs.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @throws SchedulerException       the scheduler exception
     * @throws IllegalArgumentException the illegal argument exception
     * @throws NullPointerException     the null pointer exception
     */
    public Boolean pauseJobs(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException, IllegalArgumentException, NullPointerException {
        if (isNotEmpty(keyGroupDescriptionDTO.getKeyName())) {
            quartzScheduler.pauseJob(new JobKey(keyGroupDescriptionDTO.getKeyName(), keyGroupDescriptionDTO.getGroupName()));
            return true;
        } else {
            quartzScheduler.pauseJobs(jobGroupEquals(keyGroupDescriptionDTO.getGroupName()));
            return true;
        }
    }

    /**
     * Delete jobs boolean.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the boolean
     * @throws SchedulerException the scheduler exception
     */
    public Boolean deleteJobs(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        if (isNotEmpty(keyGroupDescriptionDTO.getKeyName())) {
            return quartzScheduler.deleteJob(new JobKey(keyGroupDescriptionDTO.getKeyName(), keyGroupDescriptionDTO.getGroupName()));
        } else {
            return quartzScheduler.deleteJobs(new ArrayList<>(quartzScheduler.getJobKeys(jobGroupEquals(keyGroupDescriptionDTO.getGroupName()))));
        }
    }

    /**
     * Delete triggers boolean.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @return the boolean
     * @throws SchedulerException the scheduler exception
     */
    public boolean deleteTriggers(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        if (isNotEmpty(keyGroupDescriptionDTO.getKeyName())) {
            return quartzScheduler.unscheduleJob(new TriggerKey(keyGroupDescriptionDTO.getKeyName(), keyGroupDescriptionDTO.getGroupName()));
        } else {
            return quartzScheduler.unscheduleJobs(new ArrayList<>(quartzScheduler.getTriggerKeys(triggerGroupEquals(keyGroupDescriptionDTO.getGroupName()))));
        }
    }

    private Integer countTriggersOfJob(final JobKey jobKey) throws SchedulerException, IllegalArgumentException {
        List<? extends Trigger> triggersOfAJob = quartzScheduler.getTriggersOfJob(jobKey);
        return isEmpty(triggersOfAJob) ? 0 : triggersOfAJob.size();
    }

    private Map<String, String> createJobDataMap(@NotBlank @NotEmpty final List<APIHeaderCO> requestHeaders, @NotBlank final String requestUrl, final HttpMethod requestType) {
        Map<String, String> jobData = new HashMap<String, String>();
        requestHeaders.stream().filter(Objects::nonNull).forEach((APIHeaderCO it) -> jobData.put(format("%s_%s", REQUEST_HEADER, it.getKey()), it.getValue()));
        jobData.put(REQUEST_URL, requestUrl);
        jobData.put(REQUEST_TYPE, requestType.toString());
        return jobData;
    }

    private Map<String, Object> createMapFromJobDataMap(@NotEmpty final JobDataMap jobDataMap) throws IllegalArgumentException {
        Map<String, Object> map = new HashMap<String, Object>();
        jobDataMap.forEach(map::put);
        return map;
    }

    private Class<? extends Job> getExecutorClass(final JobExecutorClass jobExecutorClass) throws ClassNotFoundException {
        return (Class<? extends Job>) forName(jobExecutorClass.getPackageName());
    }

    private JobDetail createJobDetail(final APIJobDataCO apiJobData, final JobCO job) throws ClassNotFoundException {
        return newJob(getExecutorClass(apiJobData.getExecutorClass()))
                .requestRecovery(job.getRecover())
                .storeDurably(job.getDurability())
                .withIdentity(new JobKey(job.getDetails().getKeyName(), job.getDetails().getGroupName()))
                .withDescription(job.getDetails().getDescription())
                .setJobData(new JobDataMap(createJobDataMap(apiJobData.getRequestHeaders(), apiJobData.getRequestUrl(), apiJobData.getRequestType())))
                .build();
    }

    private TriggerBuilder getCronTriggerBuilder(final CronJobSchedulerDataCO cronJobSchedulerData, final TriggerKey triggerKey, final JobDetail jobDetail) {
        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(cronJobSchedulerData.getTrigger().getDetails().getDescription())
                .withIdentity(triggerKey)
                .withSchedule(cronSchedule(cronJobSchedulerData.getCronExpression()).inTimeZone(getDefault()));
        return getTriggerBuilder(triggerBuilder, cronJobSchedulerData.getTrigger());
    }

    private TriggerBuilder getTriggerBuilder(TriggerBuilder triggerBuilder, final TriggerCO trigger) throws InvalidRequestException {
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

    private TriggerBuilder getSimpleTriggerBuilder(final SimpleJobSchedulerDataCO simpleJobSchedulerData, final JobDetail jobDetail) throws InvalidRequestException {

        TriggerBuilder triggerBuilder = newTrigger()
                .forJob(jobDetail)
                .usingJobData(jobDetail.getJobDataMap())
                .withDescription(simpleJobSchedulerData.getTrigger().getDetails().getDescription())
                .withIdentity(new TriggerKey(simpleJobSchedulerData.getTrigger().getDetails().getKeyName(), simpleJobSchedulerData.getTrigger().getDetails().getGroupName()));

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
    public JobDetailsCO fetchJobDetailsByJobKeyNameAndJobGroupName(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        final JobKey jobKey = new JobKey(keyGroupDescriptionDTO.getKeyName(), keyGroupDescriptionDTO.getGroupName());
        final JobDetail jobDetail = quartzScheduler.getJobDetail(jobKey);
        return new JobDetailsCO(
                jobKey.getName(),
                jobKey.getGroup(),
                jobDetail.getDescription(),
                findJobExecutorClassByValue(jobDetail.getJobClass()),
                createMapFromJobDataMap(jobDetail.getJobDataMap()),
                jobDetail.isDurable(),
                countTriggersOfJob(jobKey) == 0,
                jobDetail.requestsRecovery(),
                jobDetail.isConcurrentExectionDisallowed(),
                jobDetail.isConcurrentExectionDisallowed(),
                null);
    }

    public TriggerDetailsCO fetchTriggerDetailsByTriggerKeyNameAndTriggerGroupName(final KeyGroupDescriptionDTO keyGroupDescriptionDTO) throws SchedulerException {
        final Trigger trigger = quartzScheduler.getTrigger(new TriggerKey(keyGroupDescriptionDTO.getKeyName(), keyGroupDescriptionDTO.getGroupName()));
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
}
