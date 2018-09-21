package com.vishnu.aggarwal.admin.controller.api;


/*
Created by vishnu on 13/3/18 10:56 AM
*/


import com.vishnu.aggarwal.admin.service.QuartzService;
import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.Quartz;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.SEARCH_TEXT;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.BASE_URI;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.Quartz.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.util.WebUtils.getCookie;


/**
 * The type Quartz controller.
 */
@RestController(value = "apiQuartzController")
@RequestMapping(value = BASE_URI + Quartz.BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE})
@CommonsLog
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
    @RequestMapping(value = CREATE_API_JOB, method = POST)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createUnscheduledApiJob(@RequestBody final QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.createUnscheduledApiJob(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }

    /**
     * Create scheduled api simple triggered job response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = CREATE_API_JOB_SCHEDULED_SIMPLE, method = POST)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createScheduledApiSimpleTriggeredJob(@RequestBody final QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.createScheduledApiSimpleTriggeredJob(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }

    /**
     * Create scheduled api cron triggered job response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = CREATE_API_JOB_SCHEDULED_CRON, method = POST)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createScheduledApiCronTriggeredJob(@RequestBody final QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.createScheduledApiCronTriggeredJob(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }


    /**
     * Update existing job response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = UPDATE_API_JOB, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> updateExistingJob(@RequestBody final QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.updateExistingJob(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }

    /**
     * Create new trigger response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = CREATE_SIMPLE_TRIGGER, method = POST)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createNewSimpleTriggerForJob(@RequestBody final QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.createNewSimpleTriggerForJob(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }

    /**
     * Create new cron trigger for job response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = CREATE_CRON_TRIGGER, method = POST)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createNewCronTriggerForJob(@RequestBody final QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.createNewCronTriggerForJob(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }

    /**
     * Update existing trigger response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = UPDATE_SIMPLE_TRIGGER, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> updateExistingSimpleTrigger(@RequestBody final QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.updateExistingSimpleTrigger(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }

    /**
     * Update existing cron trigger response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = UPDATE_CRON_TRIGGER, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> updateExistingCronTrigger(@RequestBody final QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.updateExistingCronTrigger(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }


    /**
     * Fetch jobs by group name response entity.
     *
     * @param httpServletRequest     the http servlet request
     * @return the response entity
     */
    @RequestMapping(value = FETCH_JOBS_OF_CURRENT_USER_GROUP, method = GET)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchJobsByJobGroupName(HttpServletRequest httpServletRequest) {
        return quartzService.fetchJobsByJobGroupName(getCookie(httpServletRequest, X_AUTH_TOKEN));
    }


    /**
     * Fetch triggers by job key name and group name response entity.
     *
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = FETCH_TRIGGERS_BY_JOB_KEY_AND_CURRENT_USER_GROUP, method = GET)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchTriggersByJobKeyNameAndJobGroupName(@RequestParam("jobKey") final String jobKeyName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.fetchTriggersByJobKeyNameAndJobGroupName(jobKeyName, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }


    /**
     * Fetch quartz details for group name response entity.
     *
     * @param httpServletRequest     the http servlet request
     * @return the response entity
     */
    @RequestMapping(value = FETCH_QUARTZ_DETAILS_BY_CURRENT_USER_GROUP, method = GET)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> fetchQuartzDetailsForJobGroupName(HttpServletRequest httpServletRequest) {
        return quartzService.fetchQuartzDetailsForJobGroupName(getCookie(httpServletRequest, X_AUTH_TOKEN));
    }


    /**
     * Resume jobs response entity.
     *
     * @param keyGroupDescriptionDTO the job key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = RESUME_JOBS, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeJobs(@RequestBody KeyGroupDescriptionDTO keyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.resumeJobs(keyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }


    /**
     * Pause jobs response entity.
     *
     * @param keyGroupDescriptionDTO the job key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = PAUSE_JOBS, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseJobs(@RequestBody KeyGroupDescriptionDTO keyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.pauseJobs(keyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }


    /**
     * Resume triggers response entity.
     *
     * @param keyGroupDescriptionDTO the trigger key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = RESUME_TRIGGERS, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeTriggers(@RequestBody KeyGroupDescriptionDTO keyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.resumeTriggers(keyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }


    /**
     * Pause triggers response entity.
     *
     * @param keyGroupDescriptionDTO the trigger key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = PAUSE_TRIGGERS, method = PUT)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseTriggers(@RequestBody KeyGroupDescriptionDTO keyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.pauseTriggers(keyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }


    /**
     * Delete jobs response entity.
     *
     * @param keyGroupDescriptionDTO the job key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = DELETE_JOBS, method = DELETE)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteJobs(@RequestBody KeyGroupDescriptionDTO keyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return quartzService.deleteJobs(keyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }


    /**
     * Delete triggers response entity.
     *
     * @param keyGroupDescriptionDTO the trigger key group name dto
     * @param httpServletRequest     the http servlet request
     * @return the response entity
     */
    @RequestMapping(value = DELETE_TRIGGERS, method = DELETE)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteTriggers(@RequestBody KeyGroupDescriptionDTO keyGroupDescriptionDTO, HttpServletRequest httpServletRequest) {
        return quartzService.deleteTriggers(keyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }

    @RequestMapping(value = JOB_KEYS_AUTOCOMPLETE, method = GET)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> jobKeysAutocomplete(@RequestParam(SEARCH_TEXT) final String searchText, HttpServletRequest httpServletRequest) {
        return quartzService.jobKeysAutocomplete(searchText, getCookie(httpServletRequest, X_AUTH_TOKEN));
    }

}
