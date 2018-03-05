package com.vishnu.aggarwal.rest.controller;

import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.dto.KeyGroupNameDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.exceptions.*;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import com.vishnu.aggarwal.rest.service.QuartzService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

import static com.vishnu.aggarwal.core.enums.JobType.API;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * The type Quartz controller.
 */
@RestController
@CommonsLog
@RequestMapping("/api/quartz")
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
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = "/job", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewJob(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, BAD_REQUEST.value(), EMPTY);
        try {
            Date scheduledJobDate = null;
            if (quartzDTO.getJob().getType().equals(API)) {
                if (isTrue(quartzDTO.getJob().getScheduled())) {
                    if (quartzDTO.getScheduleType().equals(SIMPLE)) {
                        scheduledJobDate = quartzService.createNewScheduledApiSimpleJob(quartzDTO);
                        setRestResponseVO(restResponseVO, "Job created & scheduled at " + scheduledJobDate, OK, getMessage("quartz.job.created.and.scheduled"));
                    } else if (quartzDTO.getScheduleType().equals(CRON)) {
                        scheduledJobDate = quartzService.createNewScheduledApiCronJob(quartzDTO);
                        setRestResponseVO(restResponseVO, "Job created & scheduled at " + scheduledJobDate, OK, getMessage("quartz.job.created.and.scheduled"));
                    } else {
                        throw new ScheduleTypeNotFoundException(getMessage(getMessage("no.scheduling.type.found")));
                    }
                } else {
                    quartzService.createNewUnscheduledApiJob(quartzDTO);
                    setRestResponseVO(restResponseVO, null, OK, getMessage("quartz.job.created"));
                }
            } else {
                throw new JobTypeNotFoundException(getMessage("no.job.type.found"));
            }
        } catch (JobNotScheduledException | ClassNotFoundException | SchedulerException | JobTypeNotFoundException | ScheduleTypeNotFoundException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = "/trigger", method = POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, BAD_REQUEST.value(), EMPTY);
        try {
            Date scheduledTriggerDate = null;
            if (quartzDTO.getScheduleType().equals(SIMPLE)) {
                scheduledTriggerDate = quartzService.createNewSimpleTriggerForJob(quartzDTO);
                setRestResponseVO(restResponseVO, "Trigger created and scheduled at " + scheduledTriggerDate, OK, getMessage("quartz.trigger.created.and.scheduled"));
            } else if (quartzDTO.getScheduleType().equals(CRON)) {
                scheduledTriggerDate = quartzService.createNewCronTriggerForJob(quartzDTO);
                setRestResponseVO(restResponseVO, "Trigger created and scheduled at " + scheduledTriggerDate, OK, getMessage("quartz.trigger.created.and.scheduled"));
            } else {
                throw new ScheduleTypeNotFoundException(getMessage(getMessage("no.scheduling.type.found")));
            }
        } catch (ScheduleTypeNotFoundException | TriggerNotScheduledException | SchedulerException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = "/job/{groupName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<DataTableVO<JobDetailsCO>> fetchJobsByGroupName(@PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataTableVO<JobDetailsCO> jobDetailsCODataTableVO = new DataTableVO<JobDetailsCO>(0, 0, 0, null);
        HttpStatus httpStatus = BAD_REQUEST;
        try {
            List<JobDetailsCO> jobDetails = quartzService.fetchJobDetailsByGroupName(groupName);
            setDataTableVO(jobDetailsCODataTableVO, jobDetails.size(), jobDetails.size(), jobDetails.size(), jobDetails);
            httpStatus = OK;
        } catch (JobDetailNotFoundException | SchedulerException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = "/trigger/{jobKeyName}/{groupName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<DataTableVO<TriggerDetailsCO>> fetchTriggersByJobKeyNameAndGroupName(@PathVariable("jobKeyName") String jobKeyName, @PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataTableVO<TriggerDetailsCO> triggerDetailsCODataTableVO = new DataTableVO<TriggerDetailsCO>(0, 0, 0, null);
        HttpStatus httpStatus = BAD_REQUEST;
        try {
            List<TriggerDetailsCO> triggerDetails = quartzService.fetchTriggerDetailsByJobKeyNameAndGroupName(jobKeyName, groupName);
            setDataTableVO(triggerDetailsCODataTableVO, triggerDetails.size(), triggerDetails.size(), triggerDetails.size(), triggerDetails);
            httpStatus = OK;
        } catch (TriggerDetailNotFoundException | SchedulerException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = "/details/{groupName}", method = GET, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<DataTableVO<QuartzDetailsCO>> fetchQuartzDetailsForGroupName(@PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataTableVO<QuartzDetailsCO> quartzDetailsCODataTableVO = new DataTableVO<QuartzDetailsCO>(0, 0, 0, null);
        HttpStatus httpStatus = BAD_REQUEST;
        try {
            List<QuartzDetailsCO> quartzDetails = quartzService.fetchQuartzDetailsForAGroupName(groupName);
            setDataTableVO(quartzDetailsCODataTableVO, quartzDetails.size(), quartzDetails.size(), quartzDetails.size(), quartzDetails);
            httpStatus = OK;
        } catch (QuartzDetailNotFoundException | TriggerDetailNotFoundException | JobDetailNotFoundException | SchedulerException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = "/resume/jobs", method = {POST, PUT, PATCH}, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> resumeJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, BAD_REQUEST.value(), EMPTY);
        try {
            quartzService.resumeJobs(jobKeyGroupNameDTO.getKeyName(), jobKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, TRUE, OK, getMessage("quartz.jobs.resume.success"));
        } catch (SchedulerException | ResumeJobFailureException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = "/pause/jobs", method = {POST, PUT, PATCH}, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> pauseJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, BAD_REQUEST.value(), EMPTY);
        try {
            quartzService.pauseJobs(jobKeyGroupNameDTO.getKeyName(), jobKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, TRUE, OK, getMessage("quartz.jobs.pause.success"));
        } catch (SchedulerException | PauseJobFailureException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = "/resume/triggers", method = {POST, PUT, PATCH}, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> resumeTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, BAD_REQUEST.value(), EMPTY);
        try {
            quartzService.resumeTriggers(triggerKeyGroupNameDTO.getKeyName(), triggerKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, TRUE, OK, getMessage("quartz.triggers.resume.success"));
        } catch (SchedulerException | ResumeTriggerFailureException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = "/pause/triggers", method = {POST, PUT, PATCH}, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> pauseTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, BAD_REQUEST.value(), EMPTY);
        try {
            quartzService.pauseTriggers(triggerKeyGroupNameDTO.getKeyName(), triggerKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, TRUE, OK, getMessage("quartz.triggers.pause.success"));
        } catch (SchedulerException | PauseTriggerFailureException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = "/delete/jobs", method = DELETE, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> deleteJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, BAD_REQUEST.value(), EMPTY);
        try {
            Boolean deleted = quartzService.deleteJobs(jobKeyGroupNameDTO.getKeyName(), jobKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, deleted, OK, getMessage("quartz.jobs.delete.success"));
        } catch (SchedulerException | JobDeleteFailureException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = "/delete/triggers", method = DELETE, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> deleteTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, BAD_REQUEST.value(), EMPTY);
        try {
            Boolean deleted = quartzService.deleteTriggers(triggerKeyGroupNameDTO.getKeyName(), triggerKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, deleted, OK, getMessage("quartz.triggers.delete.success"));
        } catch (SchedulerException | TriggerDeleteFailureException e) {
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
