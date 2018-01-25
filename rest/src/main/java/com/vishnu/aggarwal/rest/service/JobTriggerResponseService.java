package com.vishnu.aggarwal.rest.service;

import com.vishnu.aggarwal.core.co.DataTableCO;
import com.vishnu.aggarwal.core.dto.JobTriggerResponseDTO;
import com.vishnu.aggarwal.rest.entity.JobTriggerResponse;
import com.vishnu.aggarwal.rest.repository.JobTriggerResponseRepository;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.hibernate.transform.Transformers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@CommonsLog
public class JobTriggerResponseService {

    @Autowired
    JobTriggerResponseRepository jobTriggerResponseRepository;

    @PersistenceContext
    private EntityManager entityManager;

    private Session getSession() {
        return entityManager.unwrap(Session.class);
    }

    public void save(JobTriggerResponseDTO jobTriggerResponseDTO) {
        if(Objects.nonNull(jobTriggerResponseDTO)){
            JobTriggerResponse jobTriggerResponse = new JobTriggerResponse();
            jobTriggerResponse.setTriggerKeyName(jobTriggerResponseDTO.getTriggerKeyName());
            jobTriggerResponse.setTriggerGroupName(jobTriggerResponseDTO.getTriggerGroupName());
            jobTriggerResponse.setJobKeyName(jobTriggerResponseDTO.getJobKeyName());
            jobTriggerResponse.setJobGroupName(jobTriggerResponseDTO.getJobGroupName());
            jobTriggerResponse.setResponseCode(jobTriggerResponseDTO.getResponseCode());
            jobTriggerResponse.setResponseHeader(jobTriggerResponseDTO.getResponseHeader());
            jobTriggerResponse.setResponseBody(jobTriggerResponseDTO.getResponseBody());
            jobTriggerResponse.setFireTime(jobTriggerResponseDTO.getFireTime());
            jobTriggerResponseRepository.save(jobTriggerResponse);
        }
    }

    public List<JobTriggerResponseDTO> fetch(JobTriggerResponseDTO jobTriggerResponseDTO) {
        return (List<JobTriggerResponseDTO>) getSession().createCriteria(JobTriggerResponse.class)
        .addOrder(getCriteriaOrder(jobTriggerResponseDTO))
        .setReadOnly(Boolean.TRUE)
        .setFirstResult(jobTriggerResponseDTO.getOffset())
        .setMaxResults(jobTriggerResponseDTO.getMax())
        .add(
                getRestrictionQuery(jobTriggerResponseDTO)
        ).setResultTransformer(Transformers.aliasToBean(JobTriggerResponseDTO.class)).list();
    }

    private Criterion getRestrictionQuery(JobTriggerResponseDTO jobTriggerResponseDTO){
        if(StringUtils.isNotEmpty(jobTriggerResponseDTO.getJobKeyName()) && StringUtils.isNotEmpty(jobTriggerResponseDTO.getTriggerKeyName())){
            return Restrictions.and(
                    Restrictions.ilike("jobName", jobTriggerResponseDTO.getJobKeyName()),
                    Restrictions.ilike("triggerName", jobTriggerResponseDTO.getTriggerKeyName())
            );
        } else if(StringUtils.isNotEmpty(jobTriggerResponseDTO.getJobKeyName())){
            return Restrictions.ilike("jobName", jobTriggerResponseDTO.getJobKeyName());
        } else if(StringUtils.isNotEmpty(jobTriggerResponseDTO.getTriggerKeyName())){
            return Restrictions.ilike("triggerName", jobTriggerResponseDTO.getTriggerKeyName());
        }
        return Restrictions.isNotNull("id");
    }

    private Order getCriteriaOrder(DataTableCO dataTableCO){
        if(dataTableCO.getOrderBy().equalsIgnoreCase("desc")){
            return Order.desc(StringUtils.isEmpty(dataTableCO.getSortBy()) ? "id" : dataTableCO.getSortBy());
        } else {
            return Order.asc(StringUtils.isEmpty(dataTableCO.getSortBy()) ? "id" : dataTableCO.getSortBy());
        }
    }
}
