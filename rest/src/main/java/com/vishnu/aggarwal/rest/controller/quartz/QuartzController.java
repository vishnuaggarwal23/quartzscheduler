package com.vishnu.aggarwal.rest.controller.quartz;

import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.exceptions.InvalidRequestException;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.rest.service.QuartzService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.constants.RoleType.ROLE_USER;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Quartz.*;
import static com.vishnu.aggarwal.core.util.TypeTokenUtils.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.POST;
import static org.springframework.web.bind.annotation.RequestMethod.PUT;

@RestController
@CommonsLog
@RequestMapping(value = BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_VALUE})
@Secured({ROLE_USER})
public class QuartzController extends BaseController {

    private final QuartzService quartzService;

    @Autowired
    public QuartzController(QuartzService quartzService) {
        this.quartzService = quartzService;
    }

    @RequestMapping(value = CREATE_UPDATE_JOB, method = {PUT, POST})
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createOrUpdateJob(@RequestBody @NotBlank @NotEmpty @NotNull final QuartzDTO quartzDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, ClassNotFoundException, SchedulerException, HttpRequestMethodNotSupportedException {

        if (quartzDTO.getJob().getReplace() && httpServletRequest.getMethod().equalsIgnoreCase(POST.toString())) {
            throw new HttpRequestMethodNotSupportedException("");
        }
        if (!quartzDTO.getJob().getReplace() && httpServletRequest.getMethod().equalsIgnoreCase(PUT.toString())) {
            throw new HttpRequestMethodNotSupportedException("");
        }
        if (quartzDTO.getJob().getReplace() && !isEmpty(quartzDTO.getTriggers())) {
            throw new InvalidRequestException("");
        }
        if (quartzDTO.getJob().getReplace() && !quartzService.jobKeyExists(quartzDTO.getJob().getDetails().getKey())) {
            throw new InvalidRequestException("");
        }
        if (!quartzDTO.getJob().getReplace() && quartzService.jobKeyExists(quartzDTO.getJob().getDetails().getKey())) {
            throw new InvalidRequestException("");
        }

        checkBindingException(bindingResult);
        Map<KeyGroupDescriptionDTO, Date> returnedMap = quartzService.createOrUpdateJob(quartzDTO);
        QuartzDetailsCO quartzDetailsCO = new QuartzDetailsCO(quartzService.fetchJobDetailsByJobKeyName(quartzDTO.getJob().getDetails()), null);
        if (!isEmpty(returnedMap)) {
            List<TriggerDetailsCO> triggerDetailsCOs = new ArrayList<>();
            for (KeyGroupDescriptionDTO keyGroupDescriptionDTO : returnedMap.keySet()) {
                TriggerDetailsCO triggerDetailsCO = quartzService.fetchTriggerDetailsByTriggerKeyGroupName(keyGroupDescriptionDTO);
                triggerDetailsCO.setScheduledDate(returnedMap.get(keyGroupDescriptionDTO));
                triggerDetailsCOs.add(triggerDetailsCO);
            }
            quartzDetailsCO.setTriggerDetails(triggerDetailsCOs);
        }
        Map<String, QuartzDetailsCO> response = new HashMap<String, QuartzDetailsCO>();
        response.put("quartzDetails", quartzDetailsCO);
        return createResponseEntity(response, getHashMapOfStringAndQuartzDetailsCO());
    }

    @RequestMapping(value = CREATE_UPDATE_TRIGGER, method = {PUT, POST})
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createOrUpdateTrigger(@RequestBody @NotBlank @NotEmpty @NotNull final QuartzDTO quartzDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException, HttpRequestMethodNotSupportedException {

        if (!isEmpty(quartzDTO.getTriggers())) {
            for (TriggerCO trigger : quartzDTO.getTriggers()) {
                if (quartzService.triggerKeyExists(trigger.getDetails().getKey()) && httpServletRequest.getMethod().equalsIgnoreCase(POST.toString())) {
                    throw new HttpRequestMethodNotSupportedException("");
                }
                if (!quartzService.triggerKeyExists(trigger.getDetails().getKey()) && httpServletRequest.getMethod().equalsIgnoreCase(PUT.toString())) {
                    throw new HttpRequestMethodNotSupportedException("");
                }
            }
        }

        checkBindingException(bindingResult);
        Map<KeyGroupDescriptionDTO, Date> returnedMap = quartzService.createOrUpdateTrigger(quartzDTO);
        QuartzDetailsCO quartzDetailsCO = new QuartzDetailsCO(quartzService.fetchJobDetailsByJobKeyName(quartzDTO.getJob().getDetails()), null);
        if (!isEmpty(returnedMap)) {
            List<TriggerDetailsCO> triggerDetailsCOs = new ArrayList<>();
            for (KeyGroupDescriptionDTO keyGroupDescriptionDTO : returnedMap.keySet()) {
                TriggerDetailsCO triggerDetailsCO = quartzService.fetchTriggerDetailsByTriggerKeyGroupName(keyGroupDescriptionDTO);
                triggerDetailsCO.setScheduledDate(returnedMap.get(keyGroupDescriptionDTO));
                triggerDetailsCOs.add(triggerDetailsCO);
            }
            quartzDetailsCO.setTriggerDetails(triggerDetailsCOs);
        }
        Map<String, QuartzDetailsCO> response = new HashMap<String, QuartzDetailsCO>();
        response.put("quartzDetails", quartzDetailsCO);
        return createResponseEntity(response, getHashMapOfStringAndQuartzDetailsCO());
    }

    @GetMapping(value = LIST_JOBS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchJobsByJobGroupName() throws SchedulerException {
        return createResponseEntity
                (
                        new DataTableVO<JobDetailsCO>().createInstance(quartzService.fetchJobDetails()),
                        getDataTableVOOfJobDetailsCO()
                );
    }

    @GetMapping(value = LIST_TRIGGERS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchTriggersByJobKeyNameAndJobGroupName(@RequestParam(JOB_KEY) @NotBlank @NotEmpty @NotNull final String jobKeyName, BindingResult bindingResult) throws BindException, SchedulerException, InvalidRequestException {
        checkBindingException(bindingResult);
        return createResponseEntity
                (
                        new DataTableVO<TriggerDetailsCO>().createInstance
                                (
                                        quartzService.fetchTriggerDetailsByJobKeyGroupName
                                                (
                                                        KeyGroupDescriptionDTO.getInstance(jobKeyName, null, null)
                                                )
                                ),
                        getDataTableVOOfTriggerDetailsCO()
                );
    }

    @GetMapping(value = LIST_QUARTZ_DETAILS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchQuartzDetailsForJobGroupName() throws SchedulerException {
        return createResponseEntity
                (
                        new DataTableVO<QuartzDetailsCO>().createInstance
                                (
                                        quartzService.fetchQuartzDetails()
                                ),
                        getDataTableVOOfQuartzDetailsCO()
                );
    }

    @DeleteMapping(value = DELETE_JOB)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteJob(@RequestBody @NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(DELETED, quartzService.deleteJobs(keyGroupDescriptionDTO));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }


    @DeleteMapping(value = DELETE_JOBS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteJobs(BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(DELETED, quartzService.deleteJobs(KeyGroupDescriptionDTO.getInstance()));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @PutMapping(value = RESUME_JOB)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeJob(@RequestBody @NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(RESUMED, quartzService.resumeJobs(keyGroupDescriptionDTO));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @PutMapping(value = RESUME_JOBS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeJobs(BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(RESUMED, quartzService.resumeJobs(KeyGroupDescriptionDTO.getInstance()));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @PutMapping(value = PAUSE_JOB)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseJob(@RequestBody @NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(PAUSED, quartzService.pauseJobs(keyGroupDescriptionDTO));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @PutMapping(value = PAUSE_JOBS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseJobs(BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(PAUSED, quartzService.pauseJobs(KeyGroupDescriptionDTO.getInstance()));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @DeleteMapping(value = DELETE_TRIGGER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteTrigger(@RequestBody @NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(DELETED, quartzService.deleteTriggers(keyGroupDescriptionDTO));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @DeleteMapping(value = DELETE_TRIGGERS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteTriggers(BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(DELETED, quartzService.deleteTriggers(KeyGroupDescriptionDTO.getInstance()));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @PutMapping(value = RESUME_TRIGGER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeTrigger(@RequestBody @NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(RESUMED, quartzService.resumeTriggers(keyGroupDescriptionDTO));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @PutMapping(value = RESUME_TRIGGERS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeTriggers(BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(RESUMED, quartzService.resumeTriggers(KeyGroupDescriptionDTO.getInstance()));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @PutMapping(value = PAUSE_TRIGGER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseTrigger(@RequestBody @NotBlank @NotEmpty @NotNull final KeyGroupDescriptionDTO keyGroupDescriptionDTO, BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(PAUSED, quartzService.pauseTriggers(keyGroupDescriptionDTO));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @PutMapping(value = PAUSE_TRIGGERS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseTriggers(BindingResult bindingResult, HttpServletRequest httpServletRequest) throws BindException, SchedulerException {
        checkBindingException(bindingResult);
        HashMap<String, Boolean> response = new HashMap<String, Boolean>();
        response.put(PAUSED, quartzService.pauseTriggers(KeyGroupDescriptionDTO.getInstance()));
        return createResponseEntity(response, getHashMapOfStringAndBoolean());
    }

    @GetMapping(value = SHOW_JOB)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchJobDetail(@RequestParam(JOB_KEY) @NotNull @NotBlank @NotEmpty final String jobKey, BindingResult bindingResult) throws SchedulerException, BindException {
        checkBindingException(bindingResult);
        HashMap<String, JobDetailsCO> response = new HashMap<String, JobDetailsCO>();
        response.put("jobDetails", quartzService.fetchJobDetailsByJobKeyName(KeyGroupDescriptionDTO.getInstance(jobKey, null, null)));
        return createResponseEntity(response, getHashMapOfStringAndJobDetailsCO());
    }

    @GetMapping(value = SHOW_TRIGGER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchTriggerDetail(@RequestParam(TRIGGER_KEY) @NotNull @NotBlank @NotEmpty final String triggerKey, BindingResult bindingResult) throws SchedulerException, BindException {
        checkBindingException(bindingResult);
        HashMap<String, TriggerDetailsCO> response = new HashMap<String, TriggerDetailsCO>();
        response.put("triggerDetails", quartzService.fetchTriggerDetailsByTriggerKeyGroupName(KeyGroupDescriptionDTO.getInstance(triggerKey, null, null)));
        return createResponseEntity(response, getHashMapOfStringAndJobDetailsCO());
    }

    @GetMapping(value = JOB_KEYS_AUTOCOMPLETE)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> jobKeysAutocomplete(@RequestParam(SEARCH_TEXT) @NotBlank(message = "{searchText.cannot.be.blank}") @NotEmpty(message = "{searchText.cannot.be.empty}") @NotNull(message = "{searchText.cannot.be.null}") final String searchText, BindingResult bindingResult) throws SchedulerException, BindException {
        checkBindingException(bindingResult);
        HashMap<String, List<KeyGroupDescriptionDTO>> response = new HashMap<String, List<KeyGroupDescriptionDTO>>();
        response.put("jobDetails", quartzService.jobKeysAutocomplete(searchText));
        return createResponseEntity(response, getHashMapOfStringAndListKeyGroupDescriptionDTO());
    }
}