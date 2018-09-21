package com.vishnu.aggarwal.rest.controller;

import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.exceptions.InvalidRequestException;
import com.vishnu.aggarwal.core.validation.interfaces.*;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.rest.service.QuartzService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.JOB_KEY;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.SEARCH_TEXT;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Quartz.*;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.*;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.util.CollectionUtils.isEmpty;
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
     * Create unscheduled api job response entity.
     *
     * @param quartzDTO     the quartz dto
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException           the bind exception
     * @throws InvalidRequestException the invalid request exception
     * @throws ClassNotFoundException  the class not found exception
     * @throws SchedulerException      the scheduler exception
     */
    @RequestMapping(value = CREATE_API_JOB, method = POST)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createUnscheduledApiJob(@Validated({CreateNewUnscheduledJob.class}) @RequestBody final QuartzDTO quartzDTO, BindingResult bindingResult) throws BindException, InvalidRequestException, ClassNotFoundException, SchedulerException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        if (isTrue(quartzDTO.getJob().getScheduled())) {
            throw new InvalidRequestException("");
        }
        HashMap<String, JobDetailsCO> response = new HashMap<String, JobDetailsCO>();
        quartzService.createNewUnscheduledApiJob(quartzDTO);
        response.put("jobDetails", quartzService.fetchJobDetailsByJobKeyName(quartzDTO.getJob().getDetails()));
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndJobDetailsCO()), ACCEPTED);
    }

    /**
     * Create scheduled api simple triggered job response entity.
     *
     * @param quartzDTO     the quartz dto
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException           the bind exception
     * @throws SchedulerException      the scheduler exception
     * @throws ClassNotFoundException  the class not found exception
     * @throws InvalidRequestException the invalid request exception
     */
    @RequestMapping(value = CREATE_API_JOB_SCHEDULED_SIMPLE, method = POST)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createScheduledApiSimpleTriggeredJob(@Validated(CreateNewScheduledSimpleTriggeredJob.class) @RequestBody final QuartzDTO quartzDTO, BindingResult bindingResult) throws BindException, SchedulerException, ClassNotFoundException, InvalidRequestException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Date scheduledDate = quartzService.createNewScheduledApiSimpleJob(quartzDTO);
        JobDetailsCO jobDetailsCO = quartzService.fetchJobDetailsByJobKeyName(quartzDTO.getJob().getDetails());
        jobDetailsCO.setScheduledDate(scheduledDate);
        HashMap<String, JobDetailsCO> response = new HashMap<String, JobDetailsCO>();
        response.put("jobDetails", jobDetailsCO);
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndJobDetailsCO()), ACCEPTED);
    }

    /**
     * Create scheduled api cron triggered job response entity.
     *
     * @param quartzDTO     the quartz dto
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException          the bind exception
     * @throws SchedulerException     the scheduler exception
     * @throws ClassNotFoundException the class not found exception
     */
    @RequestMapping(value = CREATE_API_JOB_SCHEDULED_CRON, method = POST)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createScheduledApiCronTriggeredJob(@Validated(CreateNewScheduledCronTriggeredJob.class) @RequestBody final QuartzDTO quartzDTO, BindingResult bindingResult) throws BindException, SchedulerException, ClassNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Date scheduledDate = quartzService.createNewScheduledApiCronJob(quartzDTO);
        JobDetailsCO jobDetailsCO = quartzService.fetchJobDetailsByJobKeyName(quartzDTO.getJob().getDetails());
        jobDetailsCO.setScheduledDate(scheduledDate);
        HashMap<String, JobDetailsCO> response = new HashMap<String, JobDetailsCO>();
        response.put("jobDetails", jobDetailsCO);
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndJobDetailsCO()), ACCEPTED);
    }

    /**
     * Update existing job response entity.
     *
     * @param quartzDTO     the quartz dto
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException          the bind exception
     * @throws SchedulerException     the scheduler exception
     * @throws ClassNotFoundException the class not found exception
     */
    @RequestMapping(value = UPDATE_API_JOB, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> updateExistingJob(@Validated({UpdateExistingJob.class}) @RequestBody final QuartzDTO quartzDTO, BindingResult bindingResult) throws BindException, SchedulerException, ClassNotFoundException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        HashMap<String, JobDetailsCO> response = new HashMap<String, JobDetailsCO>();
        quartzService.updateExistingJob(quartzDTO);
        response.put("jobDetails", quartzService.fetchJobDetailsByJobKeyName(quartzDTO.getJob().getDetails()));
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndJobDetailsCO()), ACCEPTED);
    }

    /**
     * Create new trigger response entity.
     *
     * @param quartzDTO     the quartz dto
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException           the bind exception
     * @throws InvalidRequestException the invalid request exception
     * @throws SchedulerException      the scheduler exception
     */
    @RequestMapping(value = CREATE_SIMPLE_TRIGGER, method = POST)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createNewSimpleTriggerForJob(@Validated({CreateNewSimpleTriggerForJob.class}) @RequestBody final QuartzDTO quartzDTO, BindingResult bindingResult) throws BindException, InvalidRequestException, SchedulerException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Date scheduledDate = quartzService.createNewSimpleTriggerForJob(quartzDTO);
        TriggerDetailsCO triggerDetailsCO = quartzService.fetchTriggerDetailsByTriggerKeyName(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails());
        triggerDetailsCO.setScheduledDate(scheduledDate);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("triggerDetails", triggerDetailsCO);
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndTriggerDetailsCO()), ACCEPTED);
    }

    /**
     * Create new cron trigger for job response entity.
     *
     * @param quartzDTO     the quartz dto
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException           the bind exception
     * @throws InvalidRequestException the invalid request exception
     * @throws SchedulerException      the scheduler exception
     */
    @RequestMapping(value = CREATE_CRON_TRIGGER, method = POST)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createNewCronTriggerForJob(@Validated({CreateNewCronTriggerForJob.class}) @RequestBody final QuartzDTO quartzDTO, BindingResult bindingResult) throws BindException, InvalidRequestException, SchedulerException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Date scheduledDate = quartzService.createNewCronTriggerForJob(quartzDTO);
        TriggerDetailsCO triggerDetailsCO = quartzService.fetchTriggerDetailsByTriggerKeyName(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails());
        triggerDetailsCO.setScheduledDate(scheduledDate);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("triggerDetails", triggerDetailsCO);
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndTriggerDetailsCO()), ACCEPTED);
    }

    /**
     * Update existing trigger response entity.
     *
     * @param quartzDTO     the quartz dto
     * @param bindingResult the binding result
     * @return the response entity
     * @throws SchedulerException the scheduler exception
     * @throws BindException      the bind exception
     */
    @RequestMapping(value = UPDATE_SIMPLE_TRIGGER, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> updateExistingSimpleTrigger(@Validated({UpdateExistingSimpleTriggerForJob.class}) @RequestBody final QuartzDTO quartzDTO, BindingResult bindingResult) throws SchedulerException, BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Date scheduledDate = quartzService.updateExistingSimpleTrigger(quartzDTO);
        TriggerDetailsCO triggerDetailsCO = quartzService.fetchTriggerDetailsByTriggerKeyName(quartzDTO.getApiJobData().getSimpleJobScheduler().getTrigger().getDetails());
        triggerDetailsCO.setScheduledDate(scheduledDate);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("triggerDetails", triggerDetailsCO);
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndTriggerDetailsCO()), ACCEPTED);
    }

    /**
     * Update existing cron trigger response entity.
     *
     * @param quartzDTO     the quartz dto
     * @param bindingResult the binding result
     * @return the response entity
     * @throws SchedulerException the scheduler exception
     * @throws BindException      the bind exception
     */
    @RequestMapping(value = UPDATE_CRON_TRIGGER, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> updateExistingCronTrigger(@Validated({UpdateExistingCronTriggerForJob.class}) @RequestBody final QuartzDTO quartzDTO, BindingResult bindingResult) throws SchedulerException, BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        Date schedueldDate = quartzService.updateExistingCronTrigger(quartzDTO);
        TriggerDetailsCO triggerDetailsCO = quartzService.fetchTriggerDetailsByTriggerKeyName(quartzDTO.getApiJobData().getCronJobScheduler().getTrigger().getDetails());
        triggerDetailsCO.setScheduledDate(schedueldDate);
        HashMap<String, Object> response = new HashMap<String, Object>();
        response.put("triggerDetails", triggerDetailsCO);
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndTriggerDetailsCO()), ACCEPTED);
    }

    /**
     * Fetch jobs by group name response entity.
     *
     * @return the response entity
     * @throws SchedulerException the scheduler exception
     */
    @RequestMapping(value = FETCH_JOBS_OF_CURRENT_USER_GROUP, method = GET)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchJobsByJobGroupName() throws SchedulerException {
        List<JobDetailsCO> jobDetailsCOS = quartzService.fetchJobDetails();
        return new ResponseEntity<String>(gson().toJson(isEmpty(jobDetailsCOS) ? new DataTableVO<JobDetailsCO>(0, 0, 0, null) : new DataTableVO<JobDetailsCO>(jobDetailsCOS.size(), jobDetailsCOS.size(), jobDetailsCOS.size(), jobDetailsCOS), getDataTableVOOfJobDetailsCO()), ACCEPTED);
    }

    /**
     * Fetch triggers by job key name and group name response entity.
     *
     * @param jobKeyName    the job key name
     * @param bindingResult the binding result
     * @return the response entity
     * @throws BindException      the bind exception
     * @throws SchedulerException the scheduler exception
     */
    @RequestMapping(value = FETCH_TRIGGERS_BY_JOB_KEY_AND_CURRENT_USER_GROUP, method = GET)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchTriggersByJobKeyNameAndJobGroupName(@RequestParam(JOB_KEY) final String jobKeyName, BindingResult bindingResult) throws BindException, SchedulerException, InvalidRequestException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }
        if (isBlank(jobKeyName)) {
            throw new InvalidRequestException("");
        }

        List<TriggerDetailsCO> triggerDetailsCOS = quartzService.fetchTriggerDetailsByJobKeyName(new KeyGroupDescriptionDTO(jobKeyName, null, null));
        return new ResponseEntity<String>(gson().toJson(isEmpty(triggerDetailsCOS) ? new DataTableVO<TriggerDetailsCO>(0, 0, 0, null) : new DataTableVO<TriggerDetailsCO>(triggerDetailsCOS.size(), triggerDetailsCOS.size(), triggerDetailsCOS.size(), triggerDetailsCOS), getDataTableVOOfTriggerDetailsCO()), ACCEPTED);
    }

    /**
     * Fetch quartz details for group name response entity.
     *
     * @param bindingResult the binding result
     * @return the response entity
     * @throws SchedulerException the scheduler exception
     * @throws BindException      the bind exception
     */
    @RequestMapping(value = FETCH_QUARTZ_DETAILS_BY_CURRENT_USER_GROUP, method = GET)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchQuartzDetailsForJobGroupName(BindingResult bindingResult) throws SchedulerException, BindException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        List<QuartzDetailsCO> quartzDetails = quartzService.fetchQuartzDetails();
        return new ResponseEntity<String>(gson().toJson(isEmpty(quartzDetails) ? new DataTableVO<QuartzDetailsCO>(0, 0, 0, null) : new DataTableVO<QuartzDetailsCO>(quartzDetails.size(), quartzDetails.size(), quartzDetails.size(), quartzDetails), getDataTableVOOfQuartzDetailsCO()), ACCEPTED);
    }

    /**
     * Resume jobs response entity.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @param bindingResult          the binding result
     * @return the response entity
     * @throws BindException      the bind exception
     * @throws SchedulerException the scheduler exception
     */
    @RequestMapping(value = RESUME_JOBS, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeJobs(@Validated({ResumeJobs.class}) @RequestBody final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult) throws BindException, SchedulerException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put("resumed", quartzService.resumeJobs(keyGroupDescriptionDTO));
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndBoolean()), ACCEPTED);
    }

    /**
     * Pause jobs response entity.
     *
     * @param keyGroupDescriptionDTO the key group description dto
     * @param bindingResult          the binding result
     * @return the response entity
     * @throws BindException      the bind exception
     * @throws SchedulerException the scheduler exception
     */
    @RequestMapping(value = PAUSE_JOBS, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseJobs(@Validated({PauseJobs.class}) @RequestBody final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult) throws BindException, SchedulerException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put("resumed", quartzService.pauseJobs(keyGroupDescriptionDTO));
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndBoolean()), ACCEPTED);
    }

    /**
     * Resume triggers response entity.
     *
     * @param keyGroupDescriptionDTO the trigger key group name dto
     * @param bindingResult          the binding result
     * @return the response entity
     * @throws BindException      the bind exception
     * @throws SchedulerException the scheduler exception
     */
    @RequestMapping(value = RESUME_TRIGGERS, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeTriggers(@Validated({ResumeTriggers.class}) @RequestBody final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult) throws BindException, SchedulerException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put("resumed", quartzService.resumeTriggers(keyGroupDescriptionDTO));
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndBoolean()), ACCEPTED);
    }

    /**
     * Pause triggers response entity.
     *
     * @param keyGroupDescriptionDTO the trigger key group name dto
     * @param bindingResult          the binding result
     * @return the response entity
     * @throws BindException      the bind exception
     * @throws SchedulerException the scheduler exception
     */
    @RequestMapping(value = PAUSE_TRIGGERS, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseTriggers(@Validated({PauseTriggers.class}) @RequestBody final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult) throws BindException, SchedulerException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put("paused", quartzService.pauseTriggers(keyGroupDescriptionDTO));
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndBoolean()), ACCEPTED);
    }

    /**
     * Delete jobs response entity.
     *
     * @param keyGroupDescriptionDTO the trigger key group description dto
     * @param bindingResult          the binding result
     * @return the response entity
     * @throws BindException      the bind exception
     * @throws SchedulerException the scheduler exception
     */
    @RequestMapping(value = DELETE_JOBS, method = DELETE)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteJobs(@Validated({DeleteJobs.class}) @RequestBody final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult) throws BindException, SchedulerException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put("deleted", quartzService.deleteJobs(keyGroupDescriptionDTO));
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndBoolean()), ACCEPTED);
    }

    /**
     * Delete triggers response entity.
     *
     * @param keyGroupDescriptionDTO the trigger key group name dto
     * @param bindingResult          the binding result
     * @return the response entity
     * @throws BindException      the bind exception
     * @throws SchedulerException the scheduler exception
     */
    @RequestMapping(value = DELETE_TRIGGERS, method = DELETE)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteTriggers(@Validated({DeleteTriggers.class}) @RequestBody final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult) throws BindException, SchedulerException {
        if (bindingResult.hasErrors()) {
            throw new BindException(bindingResult);
        }

        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put("deleted", quartzService.deleteTriggers(keyGroupDescriptionDTO));
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndBoolean()), ACCEPTED);
    }

    @RequestMapping(value = JOB_KEYS_AUTOCOMPLETE, method = GET)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> jobKeysAutocomplete(@RequestParam(SEARCH_TEXT) final String searchText) throws SchedulerException {
        HashMap<String, List<KeyGroupDescriptionDTO>> response = new HashMap<String, List<KeyGroupDescriptionDTO>>();
        response.put("jobDetails", quartzService.jobKeysAutocomplete(searchText));
        return new ResponseEntity<String>(gson().toJson(response), ACCEPTED);
    }
}
