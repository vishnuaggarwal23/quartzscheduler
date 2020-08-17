package wrapper.quartz.scheduler.util.quartz.api;

import lombok.extern.slf4j.Slf4j;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.InvalidMediaTypeException;
import org.springframework.http.MediaType;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * The type Post request utility.
 */
@Slf4j
public class PostRequestUtility extends RequestUtility {
    /**
     * Instantiates a new Post request utility.
     */
    PostRequestUtility() {
        super(HttpMethod.POST);
    }

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException, InvalidMediaTypeException {
        JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
        if (CollectionUtils.isEmpty(jobDataMap)) {
            throw new JobExecutionException("");
        }
        Map<String, String> headers = this.getHeaders(jobDataMap);
        for (String headerKey : headers.keySet()) {
            if (headerKey.equalsIgnoreCase(HttpHeaders.CONTENT_TYPE)) {
                this.httpHeaders.setContentType(MediaType.parseMediaType(headers.get(headerKey)));
            } else {
                this.httpHeaders.add(headerKey, headers.get(headerKey));
            }
        }
    }
}
