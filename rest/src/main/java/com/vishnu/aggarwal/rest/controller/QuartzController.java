package com.vishnu.aggarwal.rest.controller;

import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.QuartzDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.KeyGroupNameDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.exceptions.*;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import com.vishnu.aggarwal.rest.service.QuartzService;
import lombok.extern.apachecommons.CommonsLog;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.List;

import static com.vishnu.aggarwal.core.constants.UrlMapping.Rest.Quartz.*;
import static com.vishnu.aggarwal.core.enums.JobType.API;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static org.apache.commons.lang3.BooleanUtils.isTrue;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.springframework.http.HttpStatus.valueOf;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.*;

/**
 * The type Quartz controller.
 */
@RestController
@CommonsLog
@RequestMapping(value = BASE_URI, produces = APPLICATION_JSON_UTF8_VALUE)
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
    @RequestMapping(value = CREATE_JOB, method = POST)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewJob(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            Date scheduledJobDate = null;
            if (quartzDTO.getJob().getType().equals(API)) {
                if (isTrue(quartzDTO.getJob().getScheduled())) {
                    if (quartzDTO.getScheduleType().equals(SIMPLE)) {
                        scheduledJobDate = quartzService.createNewScheduledApiSimpleJob(quartzDTO);
                        setRestResponseVO(restResponseVO, "Job created & scheduled at " + scheduledJobDate, null, getMessage("quartz.job.created.and.scheduled"));
                    } else if (quartzDTO.getScheduleType().equals(CRON)) {
                        scheduledJobDate = quartzService.createNewScheduledApiCronJob(quartzDTO);
                        setRestResponseVO(restResponseVO, "Job created & scheduled at " + scheduledJobDate, null, getMessage("quartz.job.created.and.scheduled"));
                    } else {
                        throw new ScheduleTypeNotFoundException(getMessage(getMessage("no.scheduling.type.found")));
                    }
                } else {
                    quartzService.createNewUnscheduledApiJob(quartzDTO);
                    setRestResponseVO(restResponseVO, null, null, getMessage("quartz.job.created"));
                }
            } else {
                throw new JobTypeNotFoundException(getMessage("no.job.type.found"));
            }
        } catch (JobNotScheduledException | ClassNotFoundException | SchedulerException | JobTypeNotFoundException | ScheduleTypeNotFoundException | NullPointerException e) {
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
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = UPDATE_JOB, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> updateExistingJob(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            if (quartzDTO.getJob().getType().equals(API)) {
                quartzService.updateExistingJob(quartzDTO);
                setRestResponseVO(restResponseVO, "Job updated.", null, getMessage("quartz.job.updated"));
            } else {
                throw new JobTypeNotFoundException(getMessage("no.job.type.found"));
            }
        } catch (ClassNotFoundException | SchedulerException | JobTypeNotFoundException | JobDetailNotFoundException | NullPointerException e) {
            log.error("********* Error while updating an existing job ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("********* Error while updating an existing job ********** \n");
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
     * @return the response entity
     */
    @RequestMapping(value = CREATE_TRIGGER, method = POST)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            Date scheduledTriggerDate = null;
            if (quartzDTO.getScheduleType().equals(SIMPLE)) {
                scheduledTriggerDate = quartzService.createNewSimpleTriggerForJob(quartzDTO);
                setRestResponseVO(restResponseVO, "Trigger created and scheduled at " + scheduledTriggerDate, null, getMessage("quartz.trigger.created.and.scheduled"));
            } else if (quartzDTO.getScheduleType().equals(CRON)) {
                scheduledTriggerDate = quartzService.createNewCronTriggerForJob(quartzDTO);
                setRestResponseVO(restResponseVO, "Trigger created and scheduled at " + scheduledTriggerDate, null, getMessage("quartz.trigger.created.and.scheduled"));
            } else {
                throw new ScheduleTypeNotFoundException(getMessage(getMessage("no.scheduling.type.found")));
            }
        } catch (ScheduleTypeNotFoundException | TriggerNotScheduledException | SchedulerException | NullPointerException e) {
            log.error("********* Error while creating a new trigger ********** \n");
            e.printStackTrace();
            restResponseVO.setMessage(e.getLocalizedMessage());
        } catch (Exception e) {
            log.error("********* Error while creating a new trigger ********** \n");
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
     * @return the response entity
     */
    @RequestMapping(value = UPDATE_TRIGGER, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> updateExistingTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>(null, null, EMPTY);
        try {
            Date scheduledTriggerDate = null;
            if (quartzDTO.getScheduleType().equals(SIMPLE)) {
                scheduledTriggerDate = quartzService.updateExistingSimpleTrigger(quartzDTO);
                setRestResponseVO(restResponseVO, "Trigger updated and scheduled at " + scheduledTriggerDate, null, getMessage("quartz.updated.created.and.scheduled"));
            } else if (quartzDTO.getScheduleType().equals(CRON)) {
                scheduledTriggerDate = quartzService.updateExistingCronTrigger(quartzDTO);
                setRestResponseVO(restResponseVO, "Trigger updated and scheduled at " + scheduledTriggerDate, null, getMessage("quartz.updated.created.and.scheduled"));
            } else {
                throw new ScheduleTypeNotFoundException(getMessage(getMessage("no.scheduling.type.found")));
            }
        } catch (ScheduleTypeNotFoundException | TriggerNotScheduledException | JobDetailNotFoundException | TriggerDetailNotFoundException | SchedulerException | NullPointerException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = FETCH_JOB_BY_GROUP_NAME, method = GET)
    @ResponseBody
    public ResponseEntity<RestResponseVO<DataTableVO<JobDetailsCO>>> fetchJobsByGroupName(@PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataTableVO<JobDetailsCO> jobDetailsCODataTableVO = new DataTableVO<JobDetailsCO>(0, 0, 0, null);
        try {
            List<JobDetailsCO> jobDetails = quartzService.fetchJobDetailsByGroupName(groupName);
            setDataTableVO(jobDetailsCODataTableVO, jobDetails.size(), jobDetails.size(), jobDetails.size(), jobDetails);
        } catch (JobDetailNotFoundException | SchedulerException | NullPointerException e) {
            log.error("********** Error while fetching jobs by group name ********** \n");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("********** Error while fetching jobs by group name ********** \n");
            e.printStackTrace();
        }
        return new ResponseEntity<RestResponseVO<DataTableVO<JobDetailsCO>>>(new RestResponseVO<DataTableVO<JobDetailsCO>>(jobDetailsCODataTableVO, null, null), valueOf(httpServletResponse.getStatus()));
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
    public ResponseEntity<RestResponseVO<DataTableVO<TriggerDetailsCO>>> fetchTriggersByJobKeyNameAndGroupName(@PathVariable("jobKeyName") String jobKeyName, @PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataTableVO<TriggerDetailsCO> triggerDetailsCODataTableVO = new DataTableVO<TriggerDetailsCO>(0, 0, 0, null);
        try {
            List<TriggerDetailsCO> triggerDetails = quartzService.fetchTriggerDetailsByJobKeyNameAndGroupName(jobKeyName, groupName);
            setDataTableVO(triggerDetailsCODataTableVO, triggerDetails.size(), triggerDetails.size(), triggerDetails.size(), triggerDetails);
        } catch (TriggerDetailNotFoundException | SchedulerException | NullPointerException e) {
            log.error("*********** Error while fetching triggers by job key and group name");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("*********** Error while fetching triggers by job key and group name");
            e.printStackTrace();
        }
        return new ResponseEntity<RestResponseVO<DataTableVO<TriggerDetailsCO>>>(new RestResponseVO<DataTableVO<TriggerDetailsCO>>(triggerDetailsCODataTableVO, null, null), valueOf(httpServletResponse.getStatus()));
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
    public ResponseEntity<RestResponseVO<DataTableVO<QuartzDetailsCO>>> fetchQuartzDetailsForGroupName(@PathVariable("groupName") String groupName, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        DataTableVO<QuartzDetailsCO> quartzDetailsCODataTableVO = new DataTableVO<QuartzDetailsCO>(0, 0, 0, null);
        try {
            List<QuartzDetailsCO> quartzDetails = quartzService.fetchQuartzDetailsForAGroupName(groupName);
            setDataTableVO(quartzDetailsCODataTableVO, quartzDetails.size(), quartzDetails.size(), quartzDetails.size(), quartzDetails);
        } catch (QuartzDetailNotFoundException | TriggerDetailNotFoundException | JobDetailNotFoundException | SchedulerException | NullPointerException e) {
            log.error("************* Error while fetching quartz details by group name");
            e.printStackTrace();
        } catch (Exception e) {
            log.error("************* Error while fetching quartz details by group name");
            e.printStackTrace();
        }
        return new ResponseEntity<RestResponseVO<DataTableVO<QuartzDetailsCO>>>(new RestResponseVO<DataTableVO<QuartzDetailsCO>>(quartzDetailsCODataTableVO, null, null), valueOf(httpServletResponse.getStatus()));
    }

    /**
     * Resume jobs response entity.
     *
     * @param jobKeyGroupNameDTO  the job key group name dto
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the response entity
     */
    @RequestMapping(value = RESUME_JOBS, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> resumeJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            quartzService.resumeJobs(jobKeyGroupNameDTO.getKeyName(), jobKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, TRUE, null, getMessage("quartz.jobs.resume.success"));
        } catch (SchedulerException | ResumeJobFailureException | NullPointerException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = PAUSE_JOBS, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> pauseJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            quartzService.pauseJobs(jobKeyGroupNameDTO.getKeyName(), jobKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, TRUE, null, getMessage("quartz.jobs.pause.success"));
        } catch (SchedulerException | PauseJobFailureException | NullPointerException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = RESUME_TRIGGERS, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> resumeTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            quartzService.resumeTriggers(triggerKeyGroupNameDTO.getKeyName(), triggerKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, TRUE, null, getMessage("quartz.triggers.resume.success"));
        } catch (SchedulerException | ResumeTriggerFailureException | NullPointerException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = PAUSE_TRIGGERS, method = {PUT, PATCH})
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> pauseTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            quartzService.pauseTriggers(triggerKeyGroupNameDTO.getKeyName(), triggerKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, TRUE, null, getMessage("quartz.triggers.pause.success"));
        } catch (SchedulerException | PauseTriggerFailureException | NullPointerException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = DELETE_JOBS, method = DELETE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> deleteJobs(@RequestBody KeyGroupNameDTO jobKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            Boolean deleted = quartzService.deleteJobs(jobKeyGroupNameDTO.getKeyName(), jobKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, deleted, null, getMessage("quartz.jobs.delete.success"));
        } catch (SchedulerException | JobDeleteFailureException | NullPointerException e) {
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
     * @return the response entity
     */
    @RequestMapping(value = DELETE_TRIGGERS, method = DELETE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<Boolean>> deleteTriggers(@RequestBody KeyGroupNameDTO triggerKeyGroupNameDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<Boolean> restResponseVO = new RestResponseVO<Boolean>(FALSE, null, EMPTY);
        try {
            Boolean deleted = quartzService.deleteTriggers(triggerKeyGroupNameDTO.getKeyName(), triggerKeyGroupNameDTO.getGroupName());
            setRestResponseVO(restResponseVO, deleted, null, getMessage("quartz.triggers.delete.success"));
        } catch (SchedulerException | TriggerDeleteFailureException | NullPointerException e) {
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
