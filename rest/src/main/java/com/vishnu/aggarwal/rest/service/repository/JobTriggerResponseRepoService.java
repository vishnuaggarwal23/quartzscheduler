package com.vishnu.aggarwal.rest.service.repository;

import com.vishnu.aggarwal.core.dto.JobTriggerResponseDTO;
import com.vishnu.aggarwal.rest.entity.JobTriggerResponse;
import com.vishnu.aggarwal.rest.repository.JobTriggerResponseRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

/**
 * The type Job trigger response service.
 */
@Service
@Transactional
@CommonsLog
public class JobTriggerResponseRepoService extends BaseRepoService<JobTriggerResponse, Long> {

    /**
     * The Job trigger response repository.
     */
    @Autowired
    JobTriggerResponseRepository jobTriggerResponseRepository;

    @Override
    protected Class<JobTriggerResponse> getEntityClass() {
        return JobTriggerResponse.class;
    }

    @Override
    protected JpaRepository<JobTriggerResponse, Long> getJpaRepository() {
        return jobTriggerResponseRepository;
    }

    /**
     * Save.
     *
     * @param jobTriggerResponseDTO the job trigger response dto
     * @return the job trigger response
     */
    public JobTriggerResponse save(JobTriggerResponseDTO jobTriggerResponseDTO) {
        if (Objects.nonNull(jobTriggerResponseDTO)) {
            JobTriggerResponse jobTriggerResponse = new JobTriggerResponse();
            jobTriggerResponse.setTriggerKeyName(jobTriggerResponseDTO.getTriggerKeyName());
            jobTriggerResponse.setTriggerGroupName(jobTriggerResponseDTO.getTriggerGroupName());
            jobTriggerResponse.setJobKeyName(jobTriggerResponseDTO.getJobKeyName());
            jobTriggerResponse.setJobGroupName(jobTriggerResponseDTO.getJobGroupName());
            jobTriggerResponse.setResponseCode(jobTriggerResponseDTO.getResponseCode());
            jobTriggerResponse.setResponseHeader(jobTriggerResponseDTO.getResponseHeader());
            jobTriggerResponse.setResponseBody(jobTriggerResponseDTO.getResponseBody());
            jobTriggerResponse.setFireTime(jobTriggerResponseDTO.getFireTime());
            return save(jobTriggerResponse);
        }
        return null;
    }

    /**
     * Fetch list.
     *
     * @param jobTriggerResponseDTO the job trigger response dto
     * @return the list
     */
    @SuppressWarnings("unchecked")
    public List<JobTriggerResponseDTO> fetch(JobTriggerResponseDTO jobTriggerResponseDTO) {
        return (List<JobTriggerResponseDTO>) getBaseCriteriaImpl()
                .addOrder(getCriteriaOrder(jobTriggerResponseDTO))
                .setReadOnly(Boolean.TRUE)
                .setFirstResult(jobTriggerResponseDTO.getOffset())
                .setMaxResults(jobTriggerResponseDTO.getMax())
                .add(getRestrictionQuery(jobTriggerResponseDTO))
                .setResultTransformer(Transformers.aliasToBean(JobTriggerResponseDTO.class))
                .list();
    }

    private Criterion getRestrictionQuery(JobTriggerResponseDTO jobTriggerResponseDTO) {
        if (StringUtils.isNotEmpty(jobTriggerResponseDTO.getJobKeyName()) && StringUtils.isNotEmpty(jobTriggerResponseDTO.getTriggerKeyName())) {
            return Restrictions.and(
                    Restrictions.ilike("jobName", jobTriggerResponseDTO.getJobKeyName()),
                    Restrictions.ilike("triggerName", jobTriggerResponseDTO.getTriggerKeyName())
            );
        } else if (StringUtils.isNotEmpty(jobTriggerResponseDTO.getJobKeyName())) {
            return Restrictions.ilike("jobName", jobTriggerResponseDTO.getJobKeyName());
        } else if (StringUtils.isNotEmpty(jobTriggerResponseDTO.getTriggerKeyName())) {
            return Restrictions.ilike("triggerName", jobTriggerResponseDTO.getTriggerKeyName());
        }
        return Restrictions.isNotNull("id");
    }
}
