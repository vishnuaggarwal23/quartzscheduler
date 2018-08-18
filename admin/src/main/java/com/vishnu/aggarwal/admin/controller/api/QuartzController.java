
package com.vishnu.aggarwal.admin.controller.api;


/*
Created by vishnu on 13/3/18 10:56 AM
*/


import com.vishnu.aggarwal.admin.service.QuartzService;
import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClientException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CUSTOM_REQUEST_ID;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.Quartz.*;
import static org.apache.commons.lang3.exception.ExceptionUtils.getStackTrace;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;
import static org.springframework.web.util.WebUtils.getCookie;


/**
 * The type Quartz controller.
 */
@RestController(value = "apiQuartzController")
@RequestMapping(value = BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE})
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
    @RequestMapping(value = CREATE_JOB, method = POST)
    @ResponseBody
    public ResponseEntity<String> createNewJob(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.createNewJob(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while creating a new job");
            log.error(getStackTrace(e));
            return new ResponseEntity<String>(valueOf(httpServletResponse.getStatus()));
        }
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
    public ResponseEntity<String> updateExistingJob(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.updateExistingJob(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while creating a new job");
            log.error(getStackTrace(e));
            return new ResponseEntity<>(valueOf(httpServletResponse.getStatus()));
        }
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
    public ResponseEntity<String> createNewTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.createNewTrigger(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while update an existing job");
            log.error(getStackTrace(e));
            return new ResponseEntity<String>(valueOf(httpServletResponse.getStatus()));
        }
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
    public ResponseEntity<String> updateExistingTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.updateExistingTrigger(quartzDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while updating an existing trigger");
            log.error(getStackTrace(e));
            return new ResponseEntity<String>(valueOf(httpServletResponse.getStatus()));
        }
    }


    /**
     * Fetch jobs by group name response entity.
     *
     * @param groupName           the group name
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = FETCH_JOB_BY_GROUP_NAME, method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<JobDetailsCO>> fetchJobsByGroupName(@PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.fetchJobsByGroupName(groupName, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while fetching jobs by group name");
            log.error(getStackTrace(e));
            return new ResponseEntity<DataTableVO<JobDetailsCO>>(valueOf(httpServletResponse.getStatus()));
        }
    }


    /**
     * Fetch triggers by job key name and group name response entity.
     *
     * @param jobKeyName          the job key name
     * @param groupName           the group name
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = FETCH_TRIGGER_BY_JOB_KEY_GROUP_NAME, method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<TriggerDetailsCO>> fetchTriggersByJobKeyNameAndGroupName(@PathVariable("jobKeyName") String jobKeyName, @PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.fetchTriggersByJobKeyNameAndGroupName(jobKeyName, groupName, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while fetching triggers by job key and group name");
            log.error(getStackTrace(e));
            return new ResponseEntity<DataTableVO<TriggerDetailsCO>>(valueOf(httpServletResponse.getStatus()));
        }
    }


    /**
     * Fetch quartz details for group name response entity.
     *
     * @param groupName           the group name
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = FETCH_QUARTZ_DETAILS_GROUP_NAME, method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<QuartzDetailsCO>> fetchQuartzDetailsForGroupName(@PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.fetchQuartzDetailsForGroupName(groupName, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while fetching quartz details by group name");
            log.error(getStackTrace(e));
            return new ResponseEntity<DataTableVO<QuartzDetailsCO>>(valueOf(httpServletResponse.getStatus()));
        }
    }


    /**
     * Resume jobs response entity.
     *
     * @param jobKeyGroupDescriptionDTO the job key group name dto
     * @param httpServletRequest        the http servlet request
     * @param httpServletResponse       the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = RESUME_JOBS, method = PUT)
    @ResponseBody
    public ResponseEntity<Map> resumeJobs(@RequestBody KeyGroupDescriptionDTO jobKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.resumeJobs(jobKeyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while resuming job");
            log.error(getStackTrace(e));
            return new ResponseEntity<Map>(valueOf(httpServletResponse.getStatus()));
        }
    }


    /**
     * Pause jobs response entity.
     *
     * @param jobKeyGroupDescriptionDTO the job key group name dto
     * @param httpServletRequest        the http servlet request
     * @param httpServletResponse       the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = PAUSE_JOBS, method = PUT)
    @ResponseBody
    public ResponseEntity<Map> pauseJobs(@RequestBody KeyGroupDescriptionDTO jobKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.pauseJobs(jobKeyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error(getStackTrace(e));
            return new ResponseEntity<Map>(valueOf(httpServletResponse.getStatus()));
        }
    }


    /**
     * Resume triggers response entity.
     *
     * @param triggerKeyGroupDescriptionDTO the trigger key group name dto
     * @param httpServletRequest            the http servlet request
     * @param httpServletResponse           the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = RESUME_TRIGGERS, method = PUT)
    @ResponseBody
    public ResponseEntity<Map> resumeTriggers(@RequestBody KeyGroupDescriptionDTO triggerKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.resumeTriggers(triggerKeyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while resuming trigger(s)");
            log.error(getStackTrace(e));
            return new ResponseEntity<Map>(valueOf(httpServletResponse.getStatus()));
        }
    }


    /**
     * Pause triggers response entity.
     *
     * @param triggerKeyGroupDescriptionDTO the trigger key group name dto
     * @param httpServletRequest            the http servlet request
     * @param httpServletResponse           the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = PAUSE_TRIGGERS, method = PUT)
    @ResponseBody
    public ResponseEntity<Map> pauseTriggers(@RequestBody KeyGroupDescriptionDTO triggerKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.pauseTriggers(triggerKeyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while pausing trigger(s)");
            log.error(getStackTrace(e));
            return new ResponseEntity<Map>(valueOf(httpServletResponse.getStatus()));
        }
    }


    /**
     * Delete jobs response entity.
     *
     * @param jobKeyGroupDescriptionDTO the job key group name dto
     * @param httpServletRequest        the http servlet request
     * @param httpServletResponse       the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = DELETE_JOBS, method = DELETE)
    @ResponseBody
    public ResponseEntity<Map> deleteJobs(@RequestBody KeyGroupDescriptionDTO jobKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.deleteJobs(jobKeyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while deleting job(s)");
            log.error(getStackTrace(e));
            return new ResponseEntity<Map>(valueOf(httpServletResponse.getStatus()));
        }
    }


    /**
     * Delete triggers response entity.
     *
     * @param triggerKeyGroupDescriptionDTO the trigger key group name dto
     * @param httpServletRequest            the http servlet request
     * @param httpServletResponse           the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = DELETE_TRIGGERS, method = DELETE)
    @ResponseBody
    public ResponseEntity<Map> deleteTriggers(@RequestBody KeyGroupDescriptionDTO triggerKeyGroupDescriptionDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        try {
            return quartzService.deleteTriggers(triggerKeyGroupDescriptionDTO, getCookie(httpServletRequest, X_AUTH_TOKEN));
        } catch (RestClientException e) {
            log.error("[Request Interceptor ID : " + httpServletRequest.getAttribute(CUSTOM_REQUEST_ID) + "] Error while deleting trigger(s)");
            log.error(getStackTrace(e));
            return new ResponseEntity<Map>(valueOf(httpServletResponse.getStatus()));
        }
    }

}
