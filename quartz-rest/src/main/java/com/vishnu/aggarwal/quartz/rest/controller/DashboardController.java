package com.vishnu.aggarwal.quartz.rest.controller;

/*
Created by vishnu on 15/9/18 2:42 PM
*/

import com.vishnu.aggarwal.quartz.core.controller.BaseController;
import com.vishnu.aggarwal.quartz.core.dto.DashboardDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;

import static com.vishnu.aggarwal.quartz.core.constants.UrlMapping.Rest.Dashboard.BASE_URI;
import static com.vishnu.aggarwal.quartz.core.util.TypeTokenUtils.getHashMapOfStringAndDashboardDTO;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;

@RestController
@CommonsLog
@RequestMapping(value = BASE_URI, produces = APPLICATION_JSON_UTF8_VALUE)
@Secured({"ROLE_USER"})
public class DashboardController extends BaseController {

    public ResponseEntity<String> getCounters() {
        HashMap<String, DashboardDTO> response = new HashMap<String, DashboardDTO>();
        response.put("", new DashboardDTO());
        return new ResponseEntity<String>(gson().toJson(response, getHashMapOfStringAndDashboardDTO()), ACCEPTED);
    }
}
