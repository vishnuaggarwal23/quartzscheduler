package com.vishnu.aggarwal.rest.service.repository.jpa;

import com.vishnu.aggarwal.core.dto.JobTriggerResponseDTO;
import com.vishnu.aggarwal.rest.entity.JobTriggerResponse;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import com.vishnu.aggarwal.rest.repository.jpa.JobTriggerResponseRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import javax.transaction.Transactional;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.valueOf;
import static java.util.Objects.nonNull;
import static javax.persistence.criteria.JoinType.LEFT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


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
    private final JobTriggerResponseRepository jobTriggerResponseRepository;

    private final UserService userService;

    /**
     * Instantiates a new Job trigger response repo service.
     *
     * @param jobTriggerResponseRepository the job trigger response repository
     * @param userService                  the user service
     */
    @Autowired
    public JobTriggerResponseRepoService(
            JobTriggerResponseRepository jobTriggerResponseRepository,
            UserService userService) {
        this.jobTriggerResponseRepository = jobTriggerResponseRepository;
        this.userService = userService;
    }

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
        if (nonNull(jobTriggerResponseDTO)) {
            JobTriggerResponse jobTriggerResponse = new JobTriggerResponse();
            jobTriggerResponse.setTriggerKeyName(jobTriggerResponseDTO.getTriggerKeyName());
            jobTriggerResponse.setTriggerGroupName(userService.findById(jobTriggerResponseDTO.getTriggerGroupName().getId()));
            jobTriggerResponse.setJobKeyName(jobTriggerResponseDTO.getJobKeyName());
            jobTriggerResponse.setJobGroupName(userService.findById(jobTriggerResponseDTO.getJobGroupName().getId()));
            jobTriggerResponse.setResponseCode(jobTriggerResponseDTO.getResponseCode());
            jobTriggerResponse.setResponseHeader(jobTriggerResponseDTO.getResponseHeader());
            jobTriggerResponse.setResponseBody(valueOf(jobTriggerResponseDTO.getResponseBody()));
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
     * @throws NoResultException the no result exception
     */
    @SuppressWarnings("unchecked")
    public List<JobTriggerResponseDTO> fetch(JobTriggerResponseDTO jobTriggerResponseDTO) throws NoResultException {
        CriteriaQuery<JobTriggerResponse> criteriaQuery = getBaseCriteriaSelectImpl();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        Root<JobTriggerResponse> jobTriggerResponseRoot = getRoot(criteriaQuery);
        Join<JobTriggerResponse, User> jobGroupJoin = jobTriggerResponseRoot.join("jobGroupName", LEFT);
        Join<JobTriggerResponse, User> triggerGroupJoin = jobTriggerResponseRoot.join("triggerGroupName", LEFT);
        criteriaQuery.where(getRestrictionQuery(jobTriggerResponseDTO, criteriaBuilder, jobTriggerResponseRoot, jobGroupJoin, triggerGroupJoin));
        return (List<JobTriggerResponseDTO>) selectQuery(criteriaQuery, TRUE, FALSE, jobTriggerResponseDTO);
    }

    private Predicate getRestrictionQuery(final JobTriggerResponseDTO jobTriggerResponseDTO, CriteriaBuilder criteriaBuilder, final Root<JobTriggerResponse> root, final Join<JobTriggerResponse, User> jobGroupJoin, final Join<JobTriggerResponse, User> triggerGroupJoin) {
        if (isNotBlank(jobTriggerResponseDTO.getJobKeyName()) && isNotBlank(jobTriggerResponseDTO.getTriggerKeyName())) {
            return criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jobName")), jobTriggerResponseDTO.getJobKeyName().toLowerCase()),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("triggerName")), jobTriggerResponseDTO.getTriggerKeyName().toLowerCase())
            );
        } else if (isNotBlank(jobTriggerResponseDTO.getJobKeyName())) {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("jobName")), jobTriggerResponseDTO.getJobKeyName());
        } else if (isNotBlank(jobTriggerResponseDTO.getTriggerKeyName())) {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("triggerName")), jobTriggerResponseDTO.getTriggerKeyName());
        }
        if (nonNull(jobTriggerResponseDTO.getTriggerGroupName()) && nonNull(jobTriggerResponseDTO.getJobGroupName()) && nonNull(jobTriggerResponseDTO.getJobGroupName().getId()) && nonNull(jobTriggerResponseDTO.getTriggerGroupName().getId())) {
            return criteriaBuilder.and(
                    criteriaBuilder.equal(jobGroupJoin.<Long>get("id"), jobTriggerResponseDTO.getJobGroupName().getId()),
                    criteriaBuilder.equal(triggerGroupJoin.<Long>get("id"), jobTriggerResponseDTO.getTriggerGroupName().getId())
            );
        } else if (nonNull(jobTriggerResponseDTO.getTriggerGroupName()) && nonNull(jobTriggerResponseDTO.getTriggerGroupName().getId())) {
            return criteriaBuilder.equal(triggerGroupJoin.<Long>get("id"), jobTriggerResponseDTO.getTriggerGroupName().getId());
        } else if (nonNull(jobTriggerResponseDTO.getJobGroupName()) && nonNull(jobTriggerResponseDTO.getJobGroupName().getId())) {
            return criteriaBuilder.equal(jobGroupJoin.<Long>get("id"), jobTriggerResponseDTO.getJobGroupName().getId());
        }
        return criteriaBuilder.isNotNull(root.<Long>get("id"));
    }
}
