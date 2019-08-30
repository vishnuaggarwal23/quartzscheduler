package com.vishnu.aggarwal.admin.service.feign;

/*
Created by vishnu on 17/4/18 1:08 PM
*/


import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.QuartzDTO;
import com.vishnu.aggarwal.core.vo.DataTableVO;
import com.vishnu.aggarwal.core.vo.RestResponseVO;
import feign.Headers;
import feign.Param;
import feign.RequestLine;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.X_AUTH_TOKEN;

@Headers({
        "Content-Type: " + MediaType.APPLICATION_JSON_UTF8_VALUE,
        X_AUTH_TOKEN + ": {xAuthToken}"
})
public interface QuartzFeignApiService {

    @RequestLine("POST /job")
    RestResponseVO<String> createJob(@RequestBody QuartzDTO quartzDTO, @Param("xAuthToken") String xAuthToken);

    @RequestLine("POST /trigger")
    RestResponseVO<String> createTrigger(@RequestBody QuartzDTO quartzDTO, @Param("xAuthToken") String xAuthToken);

    @RequestLine("PUT /job")
    RestResponseVO<String> updateJob(@RequestBody QuartzDTO quartzDTO, @Param("xAuthToken") String xAuthToken);

    @RequestLine("PUT /trigger")
    RestResponseVO<String> updateTrigger(@RequestBody QuartzDTO quartzDTO, @Param("xAuthToken") String xAuthToken);

    @RequestLine("DELETE /job")
    RestResponseVO<Boolean> deleteJobs(@RequestBody KeyGroupDescriptionDTO keyGroupDescriptionDTO, @Param("xAuthToken") String xAuthToken);

    @RequestLine("DELETE /trigger")
    RestResponseVO<Boolean> deleteTriggers(@RequestBody KeyGroupDescriptionDTO keyGroupDescriptionDTO, @Param("xAuthToken") String xAuthToken);

    @RequestLine("GET /job/{groupName}")
    RestResponseVO<DataTableVO<JobDetailsCO>> getJobsByGroupName(@Param("groupName") String groupName, @Param("xAuthToken") String xAuthToken);

    @RequestLine("GET /trigger/{jobKeyName}/{groupName}")
    RestResponseVO<DataTableVO<TriggerDetailsCO>> getTriggersByJobKeyAndJobGroupName(@Param("jobKeyName") String jobKeyName, @Param("jobGroupName") String jobGroupName);
}
