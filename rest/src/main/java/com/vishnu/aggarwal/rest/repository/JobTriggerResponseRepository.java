package com.vishnu.aggarwal.rest.repository;

import com.vishnu.aggarwal.rest.entity.JobTriggerResponse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobTriggerResponseRepository extends JpaRepository<JobTriggerResponse, Long>{
}
