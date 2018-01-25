package com.vishnu.aggarwal.rest.controller;

import com.vishnu.aggarwal.core.dto.JobDTO;
import com.vishnu.aggarwal.core.enums.JobType;
import com.vishnu.aggarwal.core.enums.ScheduleType;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import com.vishnu.aggarwal.rest.service.QuartzService;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.BooleanUtils;
import org.quartz.SchedulerException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@CommonsLog
@RequestMapping("/api")
public class QuartzController {

    @Autowired
    QuartzService quartzService;

    @RequestMapping(value = "/quartz/job", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public ResponseEntity<RestResponseVO<String>> createScheduledJob(@RequestBody JobDTO jobDTO, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        RestResponseVO<String> restResponseVO = new RestResponseVO<String>();
        try {
            if (jobDTO.getJobType().equals(JobType.API)) {
                if (BooleanUtils.isTrue(jobDTO.getScheduled())) {
                    if (jobDTO.getScheduleType().equals(ScheduleType.SIMPLE)) {
                        quartzService.createScheduledApiSimpleJob(jobDTO);
                        restResponseVO.setResponseCode(HttpStatus.ACCEPTED.value());
                        restResponseVO.setMessage("Job Scheduled and Created.");
                    } else if (jobDTO.getScheduleType().equals(ScheduleType.CRON)) {
                        quartzService.createScheduledApiCronJob(jobDTO);
                        restResponseVO.setResponseCode(HttpStatus.ACCEPTED.value());
                        restResponseVO.setMessage("Job Scheduled and Created.");
                    } else {
                        throw new Exception("No Job Scheduled Type Found.");
                    }
                } else {
                    quartzService.createUnscheduledApiJob(jobDTO);
                    restResponseVO.setResponseCode(HttpStatus.ACCEPTED.value());
                    restResponseVO.setMessage("Job Created.");
                }
            } else {
                throw new Exception("No Other Job Type Is Supported.");
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
