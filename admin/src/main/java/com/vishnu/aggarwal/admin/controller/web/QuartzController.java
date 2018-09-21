package com.vishnu.aggarwal.admin.controller.web;

/*
Created by vishnu on 13/3/18 11:33 AM
*/

import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.Quartz;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.enums.JobType;
import com.vishnu.aggarwal.core.enums.ScheduleType;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.REDIRECTED;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.TYPE;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.BASE_URI;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.Quartz.*;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;

/**
 * The type Quartz controller.
 */
@Controller(value = "webQuartzController")
@CommonsLog
@RequestMapping(BASE_URI + Quartz.BASE_URI)
public class QuartzController extends BaseController {

    /**
     * Create new job model and view.
     *
     * @param jobType             the job type
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the model and view
     */
    @RequestMapping(CREATE_JOB)
    public ModelAndView createNewJob(@RequestParam(TYPE) JobType jobType, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute("jobType", jobType);
        return new ModelAndView("quartz/job/api/create", modelMap);
    }

    /**
     * Create new trigger model and view.
     *
     * @return the model and view
     */
    @RequestMapping(CREATE_TRIGGER)
    public ModelAndView createNewTrigger(@RequestParam(TYPE) ScheduleType scheduleType) {
        ModelMap modelMap = new ModelMap();
        if (scheduleType.equals(SIMPLE)) {
            modelMap.put("scheduleType", SIMPLE);
            return new ModelAndView("quartz/trigger/simple/create", modelMap);
        } else if (scheduleType.equals(CRON)) {
            modelMap.put("scheduleType", CRON);
            return new ModelAndView("quartz/trigger/cron/create", modelMap);
        } else {
            return new ModelAndView("error/404");
        }
    }

    @RequestMapping(VIEW_JOB)
    public ModelAndView viewJob(@RequestParam(value = REDIRECTED, defaultValue = "false") Boolean redirected, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        ModelMap modelMap = new ModelMap();
        return new ModelAndView("quartz/job/view", modelMap);
    }

    @RequestMapping(LIST_JOBS)
    public ModelAndView listJobs(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        ModelMap modelMap = new ModelMap();
        return new ModelAndView("quartz/job/list", modelMap);
    }
}
