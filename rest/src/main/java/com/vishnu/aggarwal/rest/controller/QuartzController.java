package com.vishnu.aggarwal.rest.controller;

import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.enums.JobType;
import com.vishnu.aggarwal.core.enums.ScheduleType;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import com.vishnu.aggarwal.rest.service.QuartzService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.BooleanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@CommonsLog
@RequestMapping("/api")
public class QuartzController {

    @Autowired
    QuartzService quartzService;

    @RequestMapping(value = "/quartz/job", method = RequestMethod.POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewJob(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>();
        try {
            if (quartzDTO.getJob().getType().equals(JobType.API)) {
                if (BooleanUtils.isTrue(quartzDTO.getJob().getScheduled())) {
                    if (quartzDTO.getScheduleType().equals(ScheduleType.SIMPLE)) {
                        quartzService.createNewScheduledApiSimpleJob(quartzDTO);
                        restResponseVO.setResponseCode(HttpStatus.ACCEPTED.value());
                        restResponseVO.setMessage("quartz.job.created.and.scheduled");
                    } else if (quartzDTO.getScheduleType().equals(ScheduleType.CRON)) {
                        quartzService.createNewScheduledApiCronJob(quartzDTO);
                        restResponseVO.setResponseCode(HttpStatus.ACCEPTED.value());
                        restResponseVO.setMessage("quartz.job.created.and.scheduled");
                    } else {
                        throw new Exception("no.scheduling.type.found");
                    }
                } else {
                    quartzService.createNewUnscheduledApiJob(quartzDTO);
                    restResponseVO.setResponseCode(HttpStatus.ACCEPTED.value());
                    restResponseVO.setMessage("quartz.job.created");
                }
            } else {
                throw new Exception("no.job.type.found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setData(null);
            restResponseVO.setResponseCode(HttpStatus.BAD_REQUEST.value());
            restResponseVO.setMessage(e.getMessage());
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, HttpStatus.valueOf(restResponseVO.getResponseCode()));
    }

    @RequestMapping(value = "/quartz/trigger", method = RequestMethod.POST, consumes = APPLICATION_JSON_UTF8_VALUE, produces = APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createNewTrigger(@RequestBody QuartzDTO quartzDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse){
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>();
        try{
            if(quartzDTO.getScheduleType().equals(ScheduleType.SIMPLE)){
                quartzService.createNewSimpleTriggerForJob(quartzDTO);
                restResponseVO.setResponseCode(HttpStatus.ACCEPTED.value());
                restResponseVO.setMessage("quartz.trigger.created.and.scheduled");
            } else if(quartzDTO.getScheduleType().equals(ScheduleType.CRON)){
                quartzService.createNewCronTriggerForJob(quartzDTO);
                restResponseVO.setResponseCode(HttpStatus.ACCEPTED.value());
                restResponseVO.setMessage("quartz.trigger.created.and.scheduled");
            } else {
                throw new Exception("no.scheduling.type.found");
            }
        } catch (Exception e) {
            e.printStackTrace();
            restResponseVO.setData(null);
            restResponseVO.setResponseCode(HttpStatus.BAD_REQUEST.value());
            restResponseVO.setMessage(e.getMessage());
        }
        return new ResponseEntity<RestResponseVO<String>>(restResponseVO, HttpStatus.valueOf(restResponseVO.getResponseCode()));
    }
}
