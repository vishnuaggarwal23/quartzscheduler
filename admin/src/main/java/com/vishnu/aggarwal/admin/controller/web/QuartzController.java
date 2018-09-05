package com.vishnu.aggarwal.admin.controller.web;

/*
Created by vishnu on 13/3/18 11:33 AM
*/

import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.Quartz;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.enums.JobType;
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
     * @param httpServletRequest  the http servlet request
     * @param httpServletResponse the http servlet response
     * @return the model and view
     */
    @RequestMapping(CREATE_TRIGGER)
    public ModelAndView createNewTrigger(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        return new ModelAndView("quartz/trigger/create");
    }

    @RequestMapping(VIEW_JOB)
    public ModelAndView viewJob(@RequestParam(value = REDIRECTED, defaultValue = "false") Boolean redirected, HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        ModelMap modelMap = new ModelMap();
        return new ModelAndView("quartz/job/api/view", modelMap);
    }

    @RequestMapping(LIST_JOBS)
    public ModelAndView listJobs(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
        ModelMap modelMap = new ModelMap();

        return new ModelAndView("quartz/job/api/list", modelMap);
    }
}
