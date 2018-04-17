package com.vishnu.aggarwal.admin.controller.api;

/*
Created by vishnu on 13/3/18 10:56 AM
*/

import com.vishnu.aggarwal.admin.service.QuartzService;
import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.KeyGroupNameDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.exceptions.RestServiceCallException;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.Quartz.*;
import static java.lang.Boolean.FALSE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

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
    @Autowired
    QuartzService quartzService;

    /**
     * Create new job response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param cookie              the cookie
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = CREATE_JOB, method = POST)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewJob(@RequestBody QuartzDTO quartzDTO, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            restResponseVO = quartzService.createNewJob(quartzDTO, cookie);
        } catch (RestServiceCallException e) {
            log.error("********* Error while creating a new job ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("********* Error while creating a new job ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Update existing job response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param cookie              the cookie
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = UPDATE_JOB, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> updateExistingJob(@RequestBody QuartzDTO quartzDTO, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            restResponseVO = quartzService.updateExistingJob(quartzDTO, cookie);
        } catch (RestServiceCallException e) {
            log.error("********* Error while creating a new job ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("********* Error while creating a new job ********** \n");
            e.printStackTrace();
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
     * @param cookie              the cookie
     * @return the response entity
     */
    @RequestMapping(value = CREATE_TRIGGER, method = POST)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            restResponseVO = quartzService.createNewTrigger(quartzDTO, cookie);
        } catch (RestServiceCallException e) {
            log.error("********* Error while update an existing job ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("********* Error while update an existing job ********** \n");
            e.printStackTrace();
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
     * @param cookie              the cookie
     * @return the response entity
     */
    @RequestMapping(value = UPDATE_TRIGGER, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> updateExistingTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            restResponseVO = quartzService.updateExistingTrigger(quartzDTO, cookie);
        } catch (RestServiceCallException e) {
            log.error("********* Error while updating an existing trigger ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("********* Error while updating an existing trigger ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Fetch jobs by group name response entity.
     *
     * @param groupName           the group name
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @param cookie              the cookie
     * @return the response entity
     */
    @RequestMapping(value = FETCH_JOB_BY_GROUP_NAME, method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<JobDetailsCO>> fetchJobsByGroupName(@PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<DataTableVO<JobDetailsCO>> jobDetailsCODataTableVO = new RestResponseVO<DataTableVO<JobDetailsCO>>(new DataTableVO<JobDetailsCO>(0, 0, 0, null), null, null);
        try {
            jobDetailsCODataTableVO = quartzService.fetchJobsByGroupName(groupName, cookie);
        } catch (RestServiceCallException e) {
            log.error("********** Error while fetching jobs by group name ********** \n");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("********** Error while fetching jobs by group name ********** \n");
            e.printStackTrace();
        }
        return new ResponseEntity<DataTableVO<JobDetailsCO>>(jobDetailsCODataTableVO.getData(), valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Fetch triggers by job key name and group name response entity.
     *
     * @param jobKeyName          the job key name
     * @param groupName           the group name
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @param cookie              the cookie
     * @return the response entity
     */
    @RequestMapping(value = FETCH_TRIGGER_BY_JOB_KEY_GROUP_NAME, method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<TriggerDetailsCO>> fetchTriggersByJobKeyNameAndGroupName(@PathVariable("jobKeyName") String jobKeyName, @PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<DataTableVO<TriggerDetailsCO>> triggerDetailsCODataTableVO = new RestResponseVO<DataTableVO<TriggerDetailsCO>>(new DataTableVO<TriggerDetailsCO>(0, 0, 0, null), null, null);
        try {
            triggerDetailsCODataTableVO = quartzService.fetchTriggersByJobKeyNameAndGroupName(jobKeyName, groupName, cookie);
        } catch (RestServiceCallException e) {
            log.error("*********** Error while fetching triggers by job key and group name");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("*********** Error while fetching triggers by job key and group name");
            e.printStackTrace();
        }
        return new ResponseEntity<DataTableVO<TriggerDetailsCO>>(triggerDetailsCODataTableVO.getData(), valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Fetch quartz details for group name response entity.
     *
     * @param groupName           the group name
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @param cookie              the cookie
     * @return the response entity
     */
    @RequestMapping(value = FETCH_QUARTZ_DETAILS_GROUP_NAME, method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<QuartzDetailsCO>> fetchQuartzDetailsForGroupName(@PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<DataTableVO<QuartzDetailsCO>> quartzDetailsCODataTableVO = new RestResponseVO<DataTableVO<QuartzDetailsCO>>(new DataTableVO<QuartzDetailsCO>(0, 0, 0, null), null, null);
        try {
            quartzDetailsCODataTableVO = quartzService.fetchQuartzDetailsForGroupName(groupName, cookie);
        } catch (RestServiceCallException e) {
            log.error("************* Error while fetching quartz details by group name");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("************* Error while fetching quartz details by group name");
            e.printStackTrace();
        }
        return new ResponseEntity<DataTableVO<QuartzDetailsCO>>(quartzDetailsCODataTableVO.getData(), valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Resume jobs response entity.
     *
     * @param jobKeyGroupNameDTO  the job key group name dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @param cookie              the cookie
     * @return the response entity
     */
    @RequestMapping(value = RESUME_JOBS, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> resumeJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            restResponseVO = quartzService.resumeJobs(jobKeyGroupNameDTO, cookie);
        } catch (RestServiceCallException e) {
            log.error("************* Error while resuming job ************ \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("************* Error while resuming job ************ \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Pause jobs response entity.
     *
     * @param jobKeyGroupNameDTO  the job key group name dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @param cookie              the cookie
     * @return the response entity
     */
    @RequestMapping(value = PAUSE_JOBS, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> pauseJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            restResponseVO = quartzService.pauseJobs(jobKeyGroupNameDTO, cookie);
        } catch (RestServiceCallException e) {
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Resume triggers response entity.
     *
     * @param triggerKeyGroupNameDTO the trigger key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @param cookie                 the cookie
     * @return the response entity
     */
    @RequestMapping(value = RESUME_TRIGGERS, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> resumeTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            restResponseVO = quartzService.resumeTriggers(triggerKeyGroupNameDTO, cookie);
        } catch (RestServiceCallException e) {
            log.error("************** Error while resuming trigger(s) ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("************** Error while resuming trigger(s) ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Pause triggers response entity.
     *
     * @param triggerKeyGroupNameDTO the trigger key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @param cookie                 the cookie
     * @return the response entity
     */
    @RequestMapping(value = PAUSE_TRIGGERS, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> pauseTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            restResponseVO = quartzService.pauseTriggers(triggerKeyGroupNameDTO, cookie);
        } catch (RestServiceCallException e) {
            log.error("************ Error while pausing trigger(s) ************* \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("************ Error while pausing trigger(s) ************* \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Delete jobs response entity.
     *
     * @param jobKeyGroupNameDTO  the job key group name dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @param cookie              the cookie
     * @return the response entity
     */
    @RequestMapping(value = DELETE_JOBS, method = DELETE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> deleteJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            restResponseVO = quartzService.deleteJobs(jobKeyGroupNameDTO, cookie);
        } catch (RestServiceCallException e) {
            log.error("************ Error while deleting job(s) *************** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("************ Error while deleting job(s) *************** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Delete triggers response entity.
     *
     * @param triggerKeyGroupNameDTO the trigger key group name dto
     * @param httpServletRequest     the http servlet request
     * @param httpServletResponse    the http servlet response
     * @param cookie                 the cookie
     * @return the response entity
     */
    @RequestMapping(value = DELETE_TRIGGERS, method = DELETE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> deleteTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = X_AUTH_TOKEN) Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            restResponseVO = quartzService.deleteTriggers(triggerKeyGroupNameDTO, cookie);
        } catch (RestServiceCallException e) {
            log.error("************* Error while deleting trigger(s) *************** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("************* Error while deleting trigger(s) *************** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(httpServletResponse.getStatus()));
    }

}
