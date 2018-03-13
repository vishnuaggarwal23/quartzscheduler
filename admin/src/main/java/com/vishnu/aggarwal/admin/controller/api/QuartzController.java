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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static java.lang.Boolean.FALSE;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * The type Quartz controller.
 */
@RestController(value = "apiQuartzController")
@RequestMapping(value = "/api/quartz", produces = {APPLICATION_JSON_UTF8_VALUE})
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
    @RequestMapping(value = "/job", method = POST)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewJob(@RequestBody QuartzDTO quartzDTO, @CookieValue(name = "x-auth-token") Cookie cookie, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, OK.value(), EMPTY);
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
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
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
    @RequestMapping(value = "/trigger", method = POST)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = "x-auth-token") Cookie cookie) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, OK.value(), EMPTY);
        try {
            restResponseVO = quartzService.createNewTrigger(quartzDTO, cookie);
        } catch (RestServiceCallException e) {
            log.error("********* Error while creating a new trigger ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("********* Error while creating a new trigger ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
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
    @RequestMapping(value = "/job/{groupName}", method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<JobDetailsCO>> fetchJobsByGroupName(@PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = "x-auth-token") Cookie cookie) {
        DataTableVO<JobDetailsCO> jobDetailsCODataTableVO = new DataTableVO<JobDetailsCO>(0, 0, 0, null);
        HttpStatus httpStatus = OK;
        try {
            jobDetailsCODataTableVO = quartzService.fetchJobsByGroupName(groupName, cookie);
            httpStatus = ACCEPTED;
        } catch (RestServiceCallException e) {
            log.error("********** Error while fetching jobs by group name ********** \n");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("********** Error while fetching jobs by group name ********** \n");
            e.printStackTrace();
        }
        return new ResponseEntity<DataTableVO<JobDetailsCO>>(jobDetailsCODataTableVO, httpStatus);
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
    @RequestMapping(value = "/trigger/{jobKeyName}/{groupName}", method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<TriggerDetailsCO>> fetchTriggersByJobKeyNameAndGroupName(@PathVariable("jobKeyName") String jobKeyName, @PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = "x-auth-token") Cookie cookie) {
        DataTableVO<TriggerDetailsCO> triggerDetailsCODataTableVO = new DataTableVO<TriggerDetailsCO>(0, 0, 0, null);
        HttpStatus httpStatus = OK;
        try {
            triggerDetailsCODataTableVO = quartzService.fetchTriggersByJobKeyNameAndGroupName(jobKeyName, groupName, cookie);
            httpStatus = ACCEPTED;
        } catch (RestServiceCallException e) {
            log.error("*********** Error while fetching triggers by job key and group name");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("*********** Error while fetching triggers by job key and group name");
            e.printStackTrace();
        }
        return new ResponseEntity<DataTableVO<TriggerDetailsCO>>(triggerDetailsCODataTableVO, httpStatus);
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
    @RequestMapping(value = "/details/{groupName}", method = GET)
    @ResponseBody
    public ResponseEntity<DataTableVO<QuartzDetailsCO>> fetchQuartzDetailsForGroupName(@PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = "x-auth-token") Cookie cookie) {
        DataTableVO<QuartzDetailsCO> quartzDetailsCODataTableVO = new DataTableVO<QuartzDetailsCO>(0, 0, 0, null);
        HttpStatus httpStatus = OK;
        try {
            quartzDetailsCODataTableVO = quartzService.fetchQuartzDetailsForGroupName(groupName, cookie);
            httpStatus = ACCEPTED;
        } catch (RestServiceCallException e) {
            log.error("************* Error while fetching quartz details by group name");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("************* Error while fetching quartz details by group name");
            e.printStackTrace();
        }
        return new ResponseEntity<DataTableVO<QuartzDetailsCO>>(quartzDetailsCODataTableVO, httpStatus);
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
    @RequestMapping(value = "/resume/jobs", method = {POST, PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> resumeJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = "x-auth-token") Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, OK.value(), EMPTY);
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
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
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
    @RequestMapping(value = "/pause/jobs", method = {POST, PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> pauseJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = "x-auth-token") Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, OK.value(), EMPTY);
        try {
            restResponseVO = quartzService.pauseJobs(jobKeyGroupNameDTO, cookie);
        } catch (RestServiceCallException e) {
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
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
    @RequestMapping(value = "/resume/triggers", method = {POST, PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> resumeTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = "x-auth-token") Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, OK.value(), EMPTY);
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
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
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
    @RequestMapping(value = "/pause/triggers", method = {POST, PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> pauseTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = "x-auth-token") Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, OK.value(), EMPTY);
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
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
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
    @RequestMapping(value = "/delete/jobs", method = DELETE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> deleteJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = "x-auth-token") Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, OK.value(), EMPTY);
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
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
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
    @RequestMapping(value = "/delete/triggers", method = DELETE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> deleteTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, @CookieValue(name = "x-auth-token") Cookie cookie) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, OK.value(), EMPTY);
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
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
    }

}
