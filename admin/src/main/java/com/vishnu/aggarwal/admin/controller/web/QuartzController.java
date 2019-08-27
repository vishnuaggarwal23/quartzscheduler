package com.vishnu.aggarwal.admin.controller.web;

/*
Created by vishnu on 13/3/18 11:33 AM
*/

import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.Quartz;
import com.vishnu.aggarwal.core.controller.BaseController;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.BASE_URI;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Web.Quartz.*;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Controller(value = "webQuartzController")
@CommonsLog
@RequestMapping(BASE_URI + Quartz.BASE_URI)
public class QuartzController extends BaseController {

    @RequestMapping(CREATE_JOB)
    public ModelAndView createNewJob() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(JOB_TYPE, INVALID_JOB_TYPE);
        return new ModelAndView("quartz/job/create", modelMap);
    }

    @RequestMapping(CREATE_JOB_FRAGMENT)
    public ModelAndView createNewJob(@RequestParam(value = FRAGMENT) String fragment) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(FRAGMENT, isNotBlank(fragment) ? fragment : EMPTY_FRAGMENT);
        if (FRAGMENTS_JOB.contains(fragment)) {
            modelMap.addAttribute(fragment.contains(TYPE.toUpperCase()) ? JOB_TYPE : NEW_FORM, fragment);
            return new ModelAndView("fragments/quartz/job/create :: " + fragment, modelMap);
        } else {
            return new ModelAndView("fragments/quartz/job/create :: " + INVALID_FRAGMENT, modelMap);
        }
    }

    @RequestMapping(CREATE_TRIGGER)
    public ModelAndView createNewTrigger() {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(TRIGGER_TYPE, INVALID_TRIGGER_TYPE);
        return new ModelAndView("quartz/trigger/create", modelMap);
    }

    @RequestMapping(CREATE_TRIGGER_FRAGMENT)
    public ModelAndView createNewTrigger(@RequestParam(value = FRAGMENT) String fragment) {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(FRAGMENT, isNotBlank(fragment) ? fragment : EMPTY_FRAGMENT);
        if (FRAGMENTS_TRIGGER.contains(fragment)) {
            modelMap.addAttribute(fragment.contains(TYPE.toUpperCase()) ? TRIGGER_TYPE : NEW_FORM, fragment);
            return new ModelAndView("fragments/quartz/trigger/create :: " + fragment, modelMap);
        } else {
            return new ModelAndView("fragments/quartz/trigger/create :: " + INVALID_FRAGMENT, modelMap);
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
