package com.vishnu.aggarwal.admin.controller.api;


import com.vishnu.aggarwal.admin.service.QuartzService;
import com.vishnu.aggarwal.core.constants.UrlMapping;
import com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.Quartz;
import com.vishnu.aggarwal.core.controller.BaseController;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.*;
import static com.vishnu.aggarwal.core.constants.UrlMapping.Admin.Api.Quartz.*;
import static org.springframework.http.HttpStatus.ACCEPTED;
import static org.springframework.http.MediaType.APPLICATION_JSON_UTF8_VALUE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController(value = "apiQuartzController")
@RequestMapping(value = UrlMapping.Admin.Api.BASE_URI + Quartz.BASE_URI, produces = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_VALUE}, consumes = {APPLICATION_JSON_UTF8_VALUE, APPLICATION_JSON_VALUE})
@CommonsLog
public class QuartzController extends BaseController {

    private final QuartzService quartzService;

    @Autowired
    public QuartzController(QuartzService quartzService) {
        this.quartzService = quartzService;
    }

    @PostMapping(CREATE_UPDATE_JOB)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createJob(@RequestBody @NotNull @NotEmpty @NotBlank final QuartzDTO quartzDTO, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.createJob(quartzDTO, xAuthToken);
    }

    @PutMapping(CREATE_UPDATE_JOB)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> updateJob(@RequestBody @NotNull @NotEmpty @NotBlank final QuartzDTO quartzDTO, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.updateJob(quartzDTO, xAuthToken);
    }

    @DeleteMapping(DELETE_JOB)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteJob(@RequestBody @NotNull @NotBlank @NotEmpty final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.deleteJob(keyGroupDescriptionDTO, xAuthToken);
    }

    @DeleteMapping(DELETE_JOBS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteJobs(@CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.deleteJobs(xAuthToken);
    }

    @PutMapping(RESUME_JOB)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeJob(@RequestBody @NotNull @NotBlank @NotEmpty final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.resumeJob(keyGroupDescriptionDTO, xAuthToken);
    }

    @PutMapping(RESUME_JOBS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeJobs(@CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.resumeJobs(xAuthToken);
    }

    @PutMapping(PAUSE_JOB)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseJob(@RequestBody @NotNull @NotBlank @NotEmpty final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.pauseJob(keyGroupDescriptionDTO, xAuthToken);
    }

    @PutMapping(PAUSE_JOBS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseJobs(@CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.pauseJobs(xAuthToken);
    }

    @GetMapping(SHOW_JOB)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> showJob(@RequestParam(JOB_KEY) @NotNull @NotBlank @NotNull final String jobKey, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.showJob(jobKey, xAuthToken);
    }

    @GetMapping(LIST_JOBS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> listJobs(@CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.listJobs(xAuthToken);
    }

    @PostMapping(CREATE_UPDATE_TRIGGER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> createTrigger(@RequestBody @NotNull @NotEmpty @NotBlank final QuartzDTO quartzDTO, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.createTrigger(quartzDTO, xAuthToken);
    }

    @PutMapping(CREATE_UPDATE_TRIGGER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> updateTrigger(@RequestBody @NotNull @NotEmpty @NotBlank final QuartzDTO quartzDTO, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.updateTrigger(quartzDTO, xAuthToken);
    }

    @DeleteMapping(DELETE_TRIGGER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteTrigger(@RequestBody @NotNull @NotBlank @NotEmpty final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.deleteTrigger(keyGroupDescriptionDTO, xAuthToken);
    }

    @DeleteMapping(DELETE_TRIGGERS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> deleteTriggers(@CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.deleteTriggers(xAuthToken);
    }

    @PutMapping(RESUME_TRIGGER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeTrigger(@RequestBody @NotNull @NotBlank @NotEmpty final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.resumeTrigger(keyGroupDescriptionDTO, xAuthToken);
    }

    @PutMapping(RESUME_TRIGGERS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> resumeTriggers(@CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.resumeTriggers(xAuthToken);
    }

    @PutMapping(PAUSE_TRIGGER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseTrigger(@RequestBody @NotNull @NotBlank @NotEmpty final KeyGroupDescriptionDTO keyGroupDescriptionDTO, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.pauseTrigger(keyGroupDescriptionDTO, xAuthToken);
    }

    @PutMapping(PAUSE_TRIGGERS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> pauseTriggers(@CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.pauseTriggers(xAuthToken);
    }

    @GetMapping(SHOW_TRIGGER)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> showTrigger(@RequestParam(TRIGGER_KEY) @NotNull @NotBlank @NotNull final String triggerKey, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.showTrigger(triggerKey, xAuthToken);
    }

    @GetMapping(LIST_TRIGGERS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> listTriggers(@RequestParam(JOB_KEY) @NotNull @NotBlank @NotNull final String jobKey, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.listTriggers(jobKey, xAuthToken);
    }

    @GetMapping(LIST_QUARTZ_DETAILS)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> listQuartzDetails(@CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.listQuartzDetails(xAuthToken);
    }

    @GetMapping(JOB_KEYS_AUTOCOMPLETE)
    @ResponseBody
    @ResponseStatus(ACCEPTED)
    public ResponseEntity<String> jobKeysAutocomplete(@RequestParam(SEARCH_TEXT) @NotNull @NotBlank @NotNull final String searchText, @CookieValue(X_AUTH_TOKEN) final String xAuthToken) {
        return quartzService.jobKeysAutocomplete(searchText, xAuthToken);
    }
}