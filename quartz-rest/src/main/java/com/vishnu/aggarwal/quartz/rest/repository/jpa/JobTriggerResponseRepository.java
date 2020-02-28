package com.vishnu.aggarwal.quartz.rest.repository.jpa;

import com.vishnu.aggarwal.quartz.rest.entity.JobTriggerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

/**
 * The interface Job trigger response repository.
 */
@Repository
@Transactional
public interface JobTriggerResponseRepository extends JpaRepository<JobTriggerResponse, Long> {
}
