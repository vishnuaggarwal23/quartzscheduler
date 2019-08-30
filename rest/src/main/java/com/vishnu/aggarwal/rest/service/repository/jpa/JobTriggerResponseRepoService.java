package com.vishnu.aggarwal.rest.service.repository.jpa;

import com.vishnu.aggarwal.core.dto.JobTriggerResponseDTO;
import com.vishnu.aggarwal.rest.entity.JobTriggerResponse;
import com.vishnu.aggarwal.rest.entity.User;
import com.vishnu.aggarwal.rest.interfaces.UserService;
import com.vishnu.aggarwal.rest.repository.jpa.JobTriggerResponseRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.NoResultException;
import javax.persistence.criteria.*;
import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;
import static java.lang.String.valueOf;
import static java.util.Objects.nonNull;
import static javax.persistence.criteria.JoinType.LEFT;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Service
@Transactional
@CommonsLog
public class JobTriggerResponseRepoService extends BaseRepoService<JobTriggerResponse, Long> {

    private final JobTriggerResponseRepository jobTriggerResponseRepository;

    private final UserService userService;

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

    @CacheEvict(value = "fetchJobTriggerResponseDTOs", allEntries = true, beforeInvocation = true)
    public JobTriggerResponse save(JobTriggerResponseDTO jobTriggerResponseDTO) {
        if (nonNull(jobTriggerResponseDTO)) {
            JobTriggerResponse jobTriggerResponse = new JobTriggerResponse();
            jobTriggerResponse.setTriggerKey(jobTriggerResponseDTO.getTriggerKey());
            jobTriggerResponse.setTriggerGroup(userService.findById(jobTriggerResponseDTO.getTriggerGroup().getId()));
            jobTriggerResponse.setJobKey(jobTriggerResponseDTO.getJobKey());
            jobTriggerResponse.setJobGroup(userService.findById(jobTriggerResponseDTO.getJobGroup().getId()));
            jobTriggerResponse.setResponseCode(jobTriggerResponseDTO.getResponseCode());
            jobTriggerResponse.setResponseHeader(jobTriggerResponseDTO.getResponseHeader());
            jobTriggerResponse.setResponseBody(valueOf(jobTriggerResponseDTO.getResponseBody()));
            jobTriggerResponse.setFireTime(jobTriggerResponseDTO.getFireTime());
            return super.save(jobTriggerResponse);
        }
        return null;
    }

    @Cacheable(value = "fetchJobTriggerResponseDTOs", key = "#jobTriggerResponseDTO.toString()", unless = "#result == null")
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
        if (isNotBlank(jobTriggerResponseDTO.getJobKey()) && isNotBlank(jobTriggerResponseDTO.getTriggerKey())) {
            return criteriaBuilder.and(
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("jobName")), jobTriggerResponseDTO.getJobKey().toLowerCase()),
                    criteriaBuilder.like(criteriaBuilder.lower(root.get("triggerName")), jobTriggerResponseDTO.getTriggerKey().toLowerCase())
            );
        } else if (isNotBlank(jobTriggerResponseDTO.getJobKey())) {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("jobName")), jobTriggerResponseDTO.getJobKey());
        } else if (isNotBlank(jobTriggerResponseDTO.getTriggerKey())) {
            return criteriaBuilder.like(criteriaBuilder.lower(root.get("triggerName")), jobTriggerResponseDTO.getTriggerKey());
        }
        if (nonNull(jobTriggerResponseDTO.getTriggerGroup()) && nonNull(jobTriggerResponseDTO.getJobGroup()) && nonNull(jobTriggerResponseDTO.getJobGroup().getId()) && nonNull(jobTriggerResponseDTO.getTriggerGroup().getId())) {
            return criteriaBuilder.and(
                    criteriaBuilder.equal(jobGroupJoin.<Long>get("id"), jobTriggerResponseDTO.getJobGroup().getId()),
                    criteriaBuilder.equal(triggerGroupJoin.<Long>get("id"), jobTriggerResponseDTO.getTriggerGroup().getId())
            );
        } else if (nonNull(jobTriggerResponseDTO.getTriggerGroup()) && nonNull(jobTriggerResponseDTO.getTriggerGroup().getId())) {
            return criteriaBuilder.equal(triggerGroupJoin.<Long>get("id"), jobTriggerResponseDTO.getTriggerGroup().getId());
        } else if (nonNull(jobTriggerResponseDTO.getJobGroup()) && nonNull(jobTriggerResponseDTO.getJobGroup().getId())) {
            return criteriaBuilder.equal(jobGroupJoin.<Long>get("id"), jobTriggerResponseDTO.getJobGroup().getId());
        }
        return criteriaBuilder.isNotNull(root.<Long>get("id"));
    }
}
