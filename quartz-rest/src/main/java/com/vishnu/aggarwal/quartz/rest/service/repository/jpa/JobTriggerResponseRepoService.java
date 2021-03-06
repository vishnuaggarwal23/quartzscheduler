package com.vishnu.aggarwal.quartz.rest.service.repository.jpa;

import com.vishnu.aggarwal.quartz.core.dto.JobTriggerResponseDTO;
import com.vishnu.aggarwal.quartz.rest.entity.JobTriggerResponse;
import com.vishnu.aggarwal.quartz.rest.entity.User;
import com.vishnu.aggarwal.quartz.rest.interfaces.UserService;
import com.vishnu.aggarwal.quartz.rest.repository.jpa.JobTriggerResponseRepository;
import lombok.NonNull;
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
            @NonNull final JobTriggerResponseRepository jobTriggerResponseRepository,
            @NonNull final UserService userService) {
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
    @CacheEvict(value = "fetchJobTriggerResponseDTOs", allEntries = true, beforeInvocation = true)
    public JobTriggerResponse save(@NonNull final JobTriggerResponseDTO jobTriggerResponseDTO) {
        return super.save(new JobTriggerResponse(jobTriggerResponseDTO.getTriggerKey(), userService.findById(jobTriggerResponseDTO.getTriggerGroup().getId()), jobTriggerResponseDTO.getJobKey(), userService.findById(jobTriggerResponseDTO.getJobGroup().getId()), jobTriggerResponseDTO.getResponseCode(), jobTriggerResponseDTO.getResponseHeader(), valueOf(jobTriggerResponseDTO.getResponseBody()), jobTriggerResponseDTO.getFireTime()));
    }

    /**
     * Fetch list.
     *
     * @param jobTriggerResponseDTO the job trigger response dto
     * @return the list
     * @throws NoResultException the no result exception
     */
    @Cacheable(value = "fetchJobTriggerResponseDTOs", key = "#jobTriggerResponseDTO.toString()", unless = "#result == null")
    @SuppressWarnings("unchecked")
    public List<JobTriggerResponseDTO> fetch(@NonNull final JobTriggerResponseDTO jobTriggerResponseDTO) throws NoResultException {
        CriteriaQuery<JobTriggerResponse> criteriaQuery = getBaseCriteriaSelectImpl();
        CriteriaBuilder criteriaBuilder = getCriteriaBuilder();
        Root<JobTriggerResponse> jobTriggerResponseRoot = getRoot(criteriaQuery);
        Join<JobTriggerResponse, User> jobGroupJoin = jobTriggerResponseRoot.join("jobGroupName", LEFT);
        Join<JobTriggerResponse, User> triggerGroupJoin = jobTriggerResponseRoot.join("triggerGroupName", LEFT);
        criteriaQuery.where(getRestrictionQuery(jobTriggerResponseDTO, criteriaBuilder, jobTriggerResponseRoot, jobGroupJoin, triggerGroupJoin));
        return (List<JobTriggerResponseDTO>) selectQuery(criteriaQuery, TRUE, FALSE, jobTriggerResponseDTO);
    }

    private Predicate getRestrictionQuery(@NonNull final JobTriggerResponseDTO jobTriggerResponseDTO, @NonNull CriteriaBuilder criteriaBuilder, @NonNull final Root<JobTriggerResponse> root, @NonNull final Join<JobTriggerResponse, User> jobGroupJoin, @NonNull final Join<JobTriggerResponse, User> triggerGroupJoin) {
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
