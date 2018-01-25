package com.vishnu.aggarwal.rest.controller;

import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import com.vishnu.aggarwal.rest.service.QuartzService;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

import static com.vishnu.aggarwal.core.enums.JobType.API;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.util.CollectionUtils.isEmpty;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

/**
 * The type Quartz controller.
 */
@RestController
@CommonsLog
@RequestMapping("/api")
public class QuartzController {

    /**
     * The Quartz service.
     */
    @Autowired
    QuartzService quartzService;

    /**
     * Create new job response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/job", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewJob(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>();
        try {
            if (quartzDTO.getJob().getType().equals(API)) {
                if (isTrue(quartzDTO.getJob().getScheduled())) {
                    if (quartzDTO.getScheduleType().equals(SIMPLE)) {
                        quartzService.createNewScheduledApiSimpleJob(quartzDTO);
                        restResponseVO.setResponseCode(ACCEPTED.value());
                        restResponseVO.setMessage("quartz.job.created.and.scheduled");
                    } else if (quartzDTO.getScheduleType().equals(CRON)) {
                        quartzService.createNewScheduledApiCronJob(quartzDTO);
                        restResponseVO.setResponseCode(ACCEPTED.value());
                        restResponseVO.setMessage("quartz.job.created.and.scheduled");
                    } else {
                        throw new Exception("no.scheduling.type.found");
                    }
                } else {
                    quartzService.createNewUnscheduledApiJob(quartzDTO);
                    restResponseVO.setResponseCode(ACCEPTED.value());
                    restResponseVO.setMessage("quartz.job.created");
                }
            } else {
                throw new Exception("no.job.type.found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setData(null);
            restResponseVO.setResponseCode(BAD_REQUEST.value());
            restResponseVO.setMessage(e.getMessage());
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
    }

    /**
     * Create new trigger response entity.
     *
     * @param quartzDTO           the quartz dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/trigger", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>();
        try {
            if (quartzDTO.getScheduleType().equals(SIMPLE)) {
                quartzService.createNewSimpleTriggerForJob(quartzDTO);
                restResponseVO.setResponseCode(ACCEPTED.value());
                restResponseVO.setMessage("quartz.trigger.created.and.scheduled");
            } else if (quartzDTO.getScheduleType().equals(CRON)) {
                quartzService.createNewCronTriggerForJob(quartzDTO);
                restResponseVO.setResponseCode(ACCEPTED.value());
                restResponseVO.setMessage("quartz.trigger.created.and.scheduled");
            } else {
                throw new Exception("no.scheduling.type.found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setData(null);
            restResponseVO.setResponseCode(BAD_REQUEST.value());
            restResponseVO.setMessage(e.getMessage());
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
    }

    /**
     * Fetch jobs by group name response entity.
     *
     * @param groupName the group name
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/details/job/{groupName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<DataTableVO<JobDetailsCO>> fetchJobsByGroupName(@PathVariable("groupName") String groupName) {
        DataTableVO<JobDetailsCO> jobDetailsCODataTableVO = new DataTableVO<JobDetailsCO>();
        HttpStatus httpStatus;
        try {
            List<JobDetailsCO> jobDetails = quartzService.fetchJobDetailsByGroupName(groupName);
            if (!isEmpty(jobDetails)) {
                jobDetails.sort((o1, o2) -> o1.getKeyName().compareToIgnoreCase(o2.getKeyName()));
                jobDetailsCODataTableVO.setCount(jobDetails.size());
                jobDetailsCODataTableVO.setRecordsTotal(jobDetails.size());
                jobDetailsCODataTableVO.setRecordsFiltered(jobDetails.size());
                jobDetailsCODataTableVO.setData(jobDetails);
                httpStatus = ACCEPTED;
            } else {
                throw new Exception("no.job.details.found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = BAD_REQUEST;
            jobDetailsCODataTableVO.setData(null);
            jobDetailsCODataTableVO.setCount(0);
            jobDetailsCODataTableVO.setRecordsFiltered(0);
            jobDetailsCODataTableVO.setRecordsTotal(0);
        }
        return new ResponseEntity<DataTableVO<JobDetailsCO>>(jobDetailsCODataTableVO, httpStatus);
    }

    /**
     * Fetch triggers by job key name and group name response entity.
     *
     * @param jobKeyName the job key name
     * @param groupName  the group name
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/details/trigger/{jobKeyName}/{groupName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<DataTableVO<TriggerDetailsCO>> fetchTriggersByJobKeyNameAndGroupName(@PathVariable("jobKeyName") String jobKeyName, @PathVariable("groupName") String groupName) {
        DataTableVO<TriggerDetailsCO> triggerDetailsCODataTableVO = new DataTableVO<TriggerDetailsCO>();
        HttpStatus httpStatus;
        try {
            List<TriggerDetailsCO> triggerDetails = quartzService.fetchTriggerDetailsByJobKeyNameAndGroupName(jobKeyName, groupName);
            if (!isEmpty(triggerDetails)) {
                triggerDetails.sort(((o1, o2) -> o1.getKeyName().compareToIgnoreCase(o2.getKeyName())));
                triggerDetailsCODataTableVO.setCount(triggerDetails.size());
                triggerDetailsCODataTableVO.setRecordsTotal(triggerDetails.size());
                triggerDetailsCODataTableVO.setRecordsFiltered(triggerDetails.size());
                triggerDetailsCODataTableVO.setData(triggerDetails);
                httpStatus = ACCEPTED;
            } else {
                throw new Exception("no.triggers.details.found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = BAD_REQUEST;
            triggerDetailsCODataTableVO.setData(null);
            triggerDetailsCODataTableVO.setCount(0);
            triggerDetailsCODataTableVO.setRecordsFiltered(0);
            triggerDetailsCODataTableVO.setRecordsTotal(0);
        }
        return new ResponseEntity<DataTableVO<TriggerDetailsCO>>(triggerDetailsCODataTableVO, httpStatus);
    }

    /**
     * Fetch quartz details for group name response entity.
     *
     * @param groupName the group name
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/details/{groupName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<DataTableVO<QuartzDetailsCO>> fetchQuartzDetailsForGroupName(@PathVariable("groupName") String groupName) {
        DataTableVO<QuartzDetailsCO> quartzDetailsCODataTableVO = new DataTableVO<QuartzDetailsCO>();
        HttpStatus httpStatus;
        try {
            List<QuartzDetailsCO> quartzDetails = quartzService.fetchQuartzDetailsForAGroupName(groupName);
            if (!isEmpty(quartzDetails)) {
                quartzDetails.sort(((o1, o2) -> o1.getJobDetails().getKeyName().compareToIgnoreCase(o2.getJobDetails().getKeyName())));
                quartzDetailsCODataTableVO.setCount(quartzDetails.size());
                quartzDetailsCODataTableVO.setRecordsTotal(quartzDetails.size());
                quartzDetailsCODataTableVO.setRecordsFiltered(quartzDetails.size());
                quartzDetailsCODataTableVO.setData(quartzDetails);
                httpStatus = ACCEPTED;
            } else {
                throw new Exception("no.quartz.details.found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            httpStatus = BAD_REQUEST;
            quartzDetailsCODataTableVO.setData(null);
            quartzDetailsCODataTableVO.setCount(0);
            quartzDetailsCODataTableVO.setRecordsFiltered(0);
            quartzDetailsCODataTableVO.setRecordsTotal(0);
        }
        return new ResponseEntity<DataTableVO<QuartzDetailsCO>>(quartzDetailsCODataTableVO, httpStatus);
    }

    /**
     * Resume jobs response entity.
     *
     * @param groupName the group name
     * @param keyName   the key name
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/resume/jobs/{groupName}/{keyName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> resumeJobs(@PathVariable("groupName") String groupName, @PathVariable("keyName") String keyName) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>();
        try {
            quartzService.resumeJobs(keyName, groupName);
            restResponseVO.setData(TRUE);
            restResponseVO.setResponseCode(ACCEPTED.value());
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setData(FALSE);
            restResponseVO.setResponseCode(BAD_REQUEST.value());
            restResponseVO.setMessage(e.getMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
    }

    /**
     * Pause jobs response entity.
     *
     * @param groupName the group name
     * @param keyName   the key name
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/pause/jobs/{groupName}/{keyName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> pauseJobs(@PathVariable("groupName") String groupName, @PathVariable("keyName") String keyName) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>();
        try {
            quartzService.pauseJobs(keyName, groupName);
            restResponseVO.setData(TRUE);
            restResponseVO.setResponseCode(ACCEPTED.value());
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setData(FALSE);
            restResponseVO.setResponseCode(BAD_REQUEST.value());
            restResponseVO.setMessage(e.getMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
    }

    /**
     * Resume triggers response entity.
     *
     * @param groupName the group name
     * @param keyName   the key name
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/resume/triggers/{groupName}/{keyName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> resumeTriggers(@PathVariable("groupName") String groupName, @PathVariable("keyName") String keyName) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>();
        try {
            quartzService.resumeTriggers(keyName, groupName);
            restResponseVO.setData(TRUE);
            restResponseVO.setResponseCode(ACCEPTED.value());
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setData(FALSE);
            restResponseVO.setResponseCode(BAD_REQUEST.value());
            restResponseVO.setMessage(e.getMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
    }

    /**
     * Pause triggers response entity.
     *
     * @param groupName the group name
     * @param keyName   the key name
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/pause/triggers/{groupName}/{keyName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> pauseTriggers(@PathVariable("groupName") String groupName, @PathVariable("keyName") String keyName) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>();
        try {
            quartzService.pauseTriggers(keyName, groupName);
            restResponseVO.setData(TRUE);
            restResponseVO.setResponseCode(ACCEPTED.value());
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setData(FALSE);
            restResponseVO.setResponseCode(BAD_REQUEST.value());
            restResponseVO.setMessage(e.getMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
    }

    /**
     * Delete jobs response entity.
     *
     * @param groupName the group name
     * @param keyName   the key name
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/delete/jobs/{groupName}/{keyName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> deleteJobs(@PathVariable("groupName") String groupName, @PathVariable("keyName") String keyName) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>();
        try {
            restResponseVO.setData(quartzService.deleteJobs(keyName, groupName));
            restResponseVO.setResponseCode(ACCEPTED.value());
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setData(FALSE);
            restResponseVO.setResponseCode(BAD_REQUEST.value());
            restResponseVO.setMessage(e.getMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
    }

    /**
     * Delete triggers response entity.
     *
     * @param groupName the group name
     * @param keyName   the key name
     * @return the response entity
     */
    @RequestMapping(value = "/quartz/delete/triggers/{groupName}/{keyName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> deleteTriggers(@PathVariable("groupName") String groupName, @PathVariable("keyName") String keyName) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>();
        try {
            restResponseVO.setData(quartzService.deleteTriggers(keyName, groupName));
            restResponseVO.setResponseCode(ACCEPTED.value());
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setData(FALSE);
            restResponseVO.setResponseCode(BAD_REQUEST.value());
            restResponseVO.setMessage(e.getMessage());
        }
        return new ResponseEntity<RestResponseVO<Boolean>>(restResponseVO, valueOf(restResponseVO.getResponseCode()));
    }
}
