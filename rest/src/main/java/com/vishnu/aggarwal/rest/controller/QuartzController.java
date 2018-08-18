package com.vishnu.aggarwal.rest.controller;

import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import com.vishnu.aggarwal.rest.service.QuartzService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Quartz.*;
import static com.vishnu.aggarwal.core.enums.JobType.API;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.text.MessageFormat.format;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.util.Assert.*;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * The type Quartz controller.
 */
@RestController
@CommonsLog
@RequestMapping(value = BASE_URI, produces = APPLICATION_JSON_UTF8_VALUE)
@Secured({"ROLE_USER"})
public class QuartzController extends BaseController {

    /**
     * The Quartz service.
     */
    private final QuartzService quartzService;

    /**
     * Instantiates a new Quartz controller.
     *
     * @param quartzService the quartz service
     */
    @Autowired
    public QuartzController(QuartzService quartzService) {
        this.quartzService = quartzService;
    }

    /**
     * Create new job response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = CREATE_JOB, method = POST)
    @ResponseBody
    public ResponseEntity<Map> createNewJob(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
//        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);

        Map<String, Object> map = new HashMap<String, Object>();

        try {
            notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request.creating.new.quartz.job"))));
            notNull(quartzDTO.getJob(), formatMessage(getMessage("object.not.set", getMessage("request.creating.new.quartz.job.not.contains.job"))));
            notNull(quartzDTO.getJob().getType(), formatMessage(getMessage("object.not.set", getMessage("request.creating.new.quartz.job.not.contains.job.type"))));

            if (quartzDTO.getJob().getType().equals(API)) {
                if (isTrue(quartzDTO.getJob().getScheduled())) {
                    notNull(quartzDTO.getScheduleType(), formatMessage(getMessage("object.not.set", getMessage("request.creating.new.quartz.job.not.contains.schedule.type"))));

                    if (quartzDTO.getScheduleType().equals(SIMPLE)) {
//                        setRestResponseVO(restResponseVO, null, null, getMessage("quartz.job.created.and.scheduled.at", quartzService.createNewScheduledApiSimpleJob(quartzDTO)));
                        Date scheduledDate = quartzService.createNewScheduledApiSimpleJob(quartzDTO);
                        if (nonNull(scheduledDate)) {
                            map.put("jobScheduledDate", scheduledDate);
                            map.put("jobCreated", TRUE);
                        }
                    } else if (quartzDTO.getScheduleType().equals(CRON)) {
//                        setRestResponseVO(restResponseVO, null, null, getMessage("quartz.job.created.and.scheduled.at", quartzService.createNewScheduledApiCronJob(quartzDTO)));
                        Date scheduledDate = quartzService.createNewScheduledApiCronJob(quartzDTO);
                        if (nonNull(scheduledDate)) {
                            map.put("jobScheduledDate", scheduledDate);
                            map.put("jobCreated", TRUE);
                        }
                    }
                } else {
                    quartzService.createNewUnscheduledApiJob(quartzDTO);
//                    setRestResponseVO(restResponseVO, null, null, getMessage("quartz.job.created"));
                    map.put("jobCreated", TRUE);
                }
            }
        } catch (SchedulerException | ClassNotFoundException | IllegalArgumentException | NullPointerException | IllegalStateException e) {
            log.error(format("[Request Interceptor Id {0}] {1}", httpServletRequest.getAttribute(CUSTOM_REQUEST_ID), getMessage("error.while.creating.new.job")));
            log.error(getStackTrace(e));
            map.put("jobCreated", FALSE);
            map.put("errorMessage", e.getLocalizedMessage());
//            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<Map>(map, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Update existing job response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = UPDATE_JOB, method = PUT)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> updateExistingJob(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request.updating.existing.quartz.job"))));
            notNull(quartzDTO.getJob(), formatMessage(getMessage("object.not.set", getMessage("request.updating.existing.quartz.job.not.contains.job"))));
            notNull(quartzDTO.getJob().getType(), getMessage("object.not.set", getMessage("request.updating.existing.quartz.job.not.contains.job.type")));

            if (quartzDTO.getJob().getType().equals(API)) {
                quartzService.updateExistingJob(quartzDTO);
                setRestResponseVO(restResponseVO, null, null, getMessage("quartz.job.updated"));
            }
        } catch (SchedulerException | ClassNotFoundException | IllegalArgumentException | NullPointerException e) {
            log.error(format("[Request Interceptor Id {0}] {1}", httpServletRequest.getAttribute(CUSTOM_REQUEST_ID), getMessage("error.while.updating.existing.job")));
            log.error(getStackTrace(e));
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Create new trigger response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = CREATE_TRIGGER, method = POST)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request.creating.new.quartz.trigger"))));
            notNull(quartzDTO.getScheduleType(), formatMessage(getMessage("object.not.set", getMessage("request.creating.new.quartz.trigger.not.contains.schedule.type"))));

            if (quartzDTO.getScheduleType().equals(SIMPLE)) {
                setRestResponseVO(restResponseVO, null, null, getMessage("quartz.trigger.created.and.scheduled.at", quartzService.createNewSimpleTriggerForJob(quartzDTO)));
            } else if (quartzDTO.getScheduleType().equals(CRON)) {
                setRestResponseVO(restResponseVO, null, null, getMessage("quartz.trigger.created.and.scheduled.at", quartzService.createNewCronTriggerForJob(quartzDTO)));
            }
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error(format("[Request Interceptor Id {0}] {1}", httpServletRequest.getAttribute(CUSTOM_REQUEST_ID), getMessage("error.while.creating.new.trigger")));
            log.error(getStackTrace(e));
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Update existing trigger response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = UPDATE_TRIGGER, method = PUT)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> updateExistingTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            notNull(quartzDTO, formatMessage(getMessage("object.not.set", getMessage("request.updating.existing.quartz.trigger"))));
            notNull(quartzDTO.getScheduleType(), formatMessage(getMessage("object.not.set", getMessage("request.updating.existing.quartz.trigger.not.contains.schedule.type"))));

            if (quartzDTO.getScheduleType().equals(SIMPLE)) {
                setRestResponseVO(restResponseVO, null, null, getMessage("quartz.trigger.updated.and.scheduled.at", quartzService.updateExistingSimpleTrigger(quartzDTO)));
            } else if (quartzDTO.getScheduleType().equals(CRON)) {
                setRestResponseVO(restResponseVO, null, null, getMessage("quartz.trigger.updated.and.scheduled.at", quartzService.updateExistingCronTrigger(quartzDTO)));
            }
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error(format("[Request Interceptor Id {0}] {1}", httpServletRequest.getAttribute(CUSTOM_REQUEST_ID), getMessage("error.while.updating.existing.trigger")));
            log.error(getStackTrace(e));
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Fetch jobs by group name response entity.
     *
     * @param jobGroupName        the group name
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = FETCH_JOB_BY_JOB_GROUP_NAME, method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<JobDetailsCO>> fetchJobsByJobGroupName(@PathVariable("jobGroupName") String jobGroupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataTableVO<JobDetailsCO> jobDetailsCODataTableVO = new DataTableVO<JobDetailsCO>(0, 0, 0, null);
        try {
            hasText(jobGroupName, formatMessage(getMessage("")));

            List<JobDetailsCO> jobDetails = quartzService.fetchJobDetailsByJobGroupName(jobGroupName);
            notEmpty(jobDetails, formatMessage(getMessage("")));

            setDataTableVO(jobDetailsCODataTableVO, jobDetails.size(), jobDetails.size(), jobDetails.size(), jobDetails);
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error("[Request Interceptor Id " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while fetching list of jobs for " + jobGroupName + " job group");
            log.error(getStackTrace(e));
        }
        return new ResponseEntity<DataTableVO<JobDetailsCO>>(jobDetailsCODataTableVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Fetch triggers by job key name and group name response entity.
     *
     * @param jobKeyName          the job key name
     * @param jobGroupName        the group name
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = FETCH_TRIGGER_BY_JOB_KEY_JOB_GROUP_NAME, method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<TriggerDetailsCO>> fetchTriggersByJobKeyNameAndJobGroupName(@PathVariable("jobKeyName") String jobKeyName, @PathVariable("jobGroupName") String jobGroupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataTableVO<TriggerDetailsCO> triggerDetailsCODataTableVO = new DataTableVO<TriggerDetailsCO>(0, 0, 0, null);
        try {
            hasText(jobKeyName, formatMessage(getMessage("")));
            hasText(jobGroupName, formatMessage(getMessage("")));

            List<TriggerDetailsCO> triggerDetails = quartzService.fetchTriggerDetailsByJobKeyNameAndJobGroupName(jobKeyName, jobGroupName);
            notEmpty(triggerDetails, formatMessage(getMessage("")));

            setDataTableVO(triggerDetailsCODataTableVO, triggerDetails.size(), triggerDetails.size(), triggerDetails.size(), triggerDetails);
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error("[Request Interceptor Id " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while fetching list of triggers for " + jobGroupName + " job group and " + jobKeyName + " job key.");
            log.error(getStackTrace(e));
        }
        return new ResponseEntity<DataTableVO<TriggerDetailsCO>>(triggerDetailsCODataTableVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Fetch quartz details for group name response entity.
     *
     * @param jobGroupName        the group name
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = FETCH_QUARTZ_DETAILS_JOB_GROUP_NAME, method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<QuartzDetailsCO>> fetchQuartzDetailsForGroupName(@PathVariable("jobGroupName") String jobGroupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataTableVO<QuartzDetailsCO> quartzDetailsCODataTableVO = new DataTableVO<QuartzDetailsCO>(0, 0, 0, null);
        try {
            hasText(jobGroupName, formatMessage(getMessage("")));

            List<QuartzDetailsCO> quartzDetails = quartzService.fetchQuartzDetailsForJobGroupName(jobGroupName);
            notEmpty(quartzDetails, formatMessage(getMessage("")));

            setDataTableVO(quartzDetailsCODataTableVO, quartzDetails.size(), quartzDetails.size(), quartzDetails.size(), quartzDetails);
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error("[Request Interceptor Id " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while fetching list of overall quartz details for " + jobGroupName + "group");
            log.error(getStackTrace(e));
        }
        return new ResponseEntity<DataTableVO<QuartzDetailsCO>>(quartzDetailsCODataTableVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Resume jobs response entity.
     *
     * @param jobKeyGroupDescriptionDTO  the job key group name dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = RESUME_JOBS, method = PUT)
    @ResponseBody
    public ResponseEntity<Map> resumeJobs(@RequestBody KeyGroupDescriptionDTO jobKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Boolean jobResumed = FALSE;
        try {
            notNull(jobKeyGroupDescriptionDTO, formatMessage(getMessage("")));
            hasText(jobKeyGroupDescriptionDTO.getKeyName(), formatMessage(getMessage("")));
            hasText(jobKeyGroupDescriptionDTO.getGroupName(), formatMessage(getMessage("")));

            quartzService.resumeJobs(jobKeyGroupDescriptionDTO.getKeyName(), jobKeyGroupDescriptionDTO.getGroupName());
//            setRestResponseVO(restResponseVO, TRUE, null, getMessage("quartz.jobs.resume.success"));
            jobResumed = TRUE;
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error("[Request Interceptor Id " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while resuming job");
            log.error(getStackTrace(e));
        }
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("jobResumed", jobResumed);
        return new ResponseEntity<Map>(map, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Pause jobs response entity.
     *
     * @param jobKeyGroupDescriptionDTO  the job key group name dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = PAUSE_JOBS, method = PUT)
    @ResponseBody
    public ResponseEntity<Map> pauseJobs(@RequestBody KeyGroupDescriptionDTO jobKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Boolean jobPaused = FALSE;
        try {
            notNull(jobKeyGroupDescriptionDTO, formatMessage(getMessage("")));
            hasText(jobKeyGroupDescriptionDTO.getKeyName(), formatMessage(getMessage("")));
            hasText(jobKeyGroupDescriptionDTO.getGroupName(), formatMessage(getMessage("")));

            quartzService.pauseJobs(jobKeyGroupDescriptionDTO.getKeyName(), jobKeyGroupDescriptionDTO.getGroupName());
//            setRestResponseVO(restResponseVO, TRUE, null, getMessage("quartz.jobs.pause.success"));
            jobPaused = TRUE;
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error("[Request Interceptor Id " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while pausing job");
            log.error(getStackTrace(e));
        }
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("jobPaused", jobPaused);
        return new ResponseEntity<Map>(map, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Resume triggers response entity.
     *
     * @param triggerKeyGroupDescriptionDTO the trigger key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = RESUME_TRIGGERS, method = PUT)
    @ResponseBody
    public ResponseEntity<Map> resumeTriggers(@RequestBody KeyGroupDescriptionDTO triggerKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Boolean triggerResumed = FALSE;
        try {
            notNull(triggerKeyGroupDescriptionDTO, formatMessage(getMessage("")));
            hasText(triggerKeyGroupDescriptionDTO.getKeyName(), formatMessage(getMessage("")));
            hasText(triggerKeyGroupDescriptionDTO.getGroupName(), formatMessage(getMessage("")));

            quartzService.resumeTriggers(triggerKeyGroupDescriptionDTO.getKeyName(), triggerKeyGroupDescriptionDTO.getGroupName());
//            setRestResponseVO(restResponseVO, TRUE, null, getMessage("quartz.triggers.resume.success"));
            triggerResumed = TRUE;
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error("[Request Interceptor Id " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while resuming trigger");
            log.error(getStackTrace(e));
        }
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("triggerResumed", triggerResumed);
        return new ResponseEntity<Map>(map, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Pause triggers response entity.
     *
     * @param triggerKeyGroupDescriptionDTO the trigger key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = PAUSE_TRIGGERS, method = PUT)
    @ResponseBody
    public ResponseEntity<Map> pauseTriggers(@RequestBody KeyGroupDescriptionDTO triggerKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Boolean triggerPaused = FALSE;
        try {
            notNull(triggerKeyGroupDescriptionDTO, formatMessage(getMessage("")));
            hasText(triggerKeyGroupDescriptionDTO.getKeyName(), formatMessage(getMessage("")));
            hasText(triggerKeyGroupDescriptionDTO.getGroupName(), formatMessage(getMessage("")));

            quartzService.pauseTriggers(triggerKeyGroupDescriptionDTO.getKeyName(), triggerKeyGroupDescriptionDTO.getGroupName());
//            setRestResponseVO(restResponseVO, TRUE, null, getMessage("quartz.triggers.pause.success"));
            triggerPaused = TRUE;
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error("[Request Interceptor Id " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while pausing trigger");
            log.error(getStackTrace(e));
        }
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("triggerPaused", triggerPaused);
        return new ResponseEntity<Map>(map, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Delete jobs response entity.
     *
     * @param jobKeyGroupDescriptionDTO  the job key group name dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = DELETE_JOBS, method = DELETE)
    @ResponseBody
    public ResponseEntity<Map> deleteJobs(@RequestBody KeyGroupDescriptionDTO jobKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Boolean jobDeleted = FALSE;
        try {
            notNull(jobKeyGroupDescriptionDTO, formatMessage(getMessage("")));
            hasText(jobKeyGroupDescriptionDTO.getKeyName(), formatMessage(getMessage("")));
            hasText(jobKeyGroupDescriptionDTO.getGroupName(), formatMessage(getMessage("")));

//            setRestResponseVO(restResponseVO, quartzService.deleteJobs(jobKeyGroupDescriptionDTO.getKeyName(), jobKeyGroupDescriptionDTO.getGroupName()), null, getMessage("quartz.jobs.delete.success"));
            jobDeleted = quartzService.deleteJobs(jobKeyGroupDescriptionDTO.getKeyName(), jobKeyGroupDescriptionDTO.getGroupName());
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error("[Request Interceptor Id " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while deleting job " + jobKeyGroupDescriptionDTO.toString());
            log.error(getStackTrace(e));
        }
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("jobDeleted", jobDeleted);
        return new ResponseEntity<Map>(map, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Delete triggers response entity.
     *
     * @param triggerKeyGroupDescriptionDTO the trigger key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = DELETE_TRIGGERS, method = DELETE)
    @ResponseBody
    public ResponseEntity<Map> deleteTriggers(@RequestBody KeyGroupDescriptionDTO triggerKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        Boolean triggerDeleted = FALSE;
        try {
            notNull(triggerKeyGroupDescriptionDTO, formatMessage(getMessage("")));
            hasText(triggerKeyGroupDescriptionDTO.getKeyName(), formatMessage(getMessage("")));
            hasText(triggerKeyGroupDescriptionDTO.getGroupName(), formatMessage(getMessage("")));

//            setRestResponseVO(restResponseVO, quartzService.deleteTriggers(triggerKeyGroupDescriptionDTO.getKeyName(), triggerKeyGroupDescriptionDTO.getGroupName()), null, getMessage("quartz.triggers.delete.success"));
            triggerDeleted = quartzService.deleteTriggers(triggerKeyGroupDescriptionDTO.getKeyName(), triggerKeyGroupDescriptionDTO.getGroupName());
        } catch (SchedulerException | IllegalArgumentException | NullPointerException e) {
            log.error("[Request Interceptor Id " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while deleting job " + triggerKeyGroupDescriptionDTO.toString());
            log.error(getStackTrace(e));
        }
        Map<String, Boolean> map = new HashMap<String, Boolean>();
        map.put("triggerDeleted", triggerDeleted);
        return new ResponseEntity<Map>(map, valueOf(httpServletResponse.getStatus()));
    }
}
