package com.vishnu.aggarwal.rest.util;

/*
Created by vishnu on 14/8/18 2:45 PM
*/

import com.vishnu.aggarwal.core.co.JobDetailsCO;
import com.vishnu.aggarwal.core.co.TriggerDetailsCO;
import com.vishnu.aggarwal.core.dto.AuthorityDTO;
import com.vishnu.aggarwal.core.dto.KeyGroupDescriptionDTO;
import com.vishnu.aggarwal.core.dto.UserAuthenticationDTO;
import com.vishnu.aggarwal.core.dto.UserDTO;
import com.vishnu.aggarwal.rest.entity.User;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;

import static com.vishnu.aggarwal.core.constants.ApplicationConstants.CREATED_DATE;
import static com.vishnu.aggarwal.core.constants.ApplicationConstants.UPDATED_DATE;
import static com.vishnu.aggarwal.core.enums.JobExecutorClass.findJobExecutorClassByValue;
import static com.vishnu.aggarwal.core.enums.ScheduleType.CRON;
import static com.vishnu.aggarwal.core.enums.ScheduleType.SIMPLE;
import static com.vishnu.aggarwal.core.enums.Status.ACTIVE;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;

public class DTOConversion {

    public static UserAuthenticationDTO convertFromUserDTO(UserDTO userDTO, Boolean isAuthenticated, String xAuthToken) throws NullPointerException, IllegalArgumentException {
        UserAuthenticationDTO userAuthenticationDTO = new UserAuthenticationDTO();
        userAuthenticationDTO.setIsAuthenticated(isAuthenticated);
        userAuthenticationDTO.setXAuthToken(xAuthToken);
        userAuthenticationDTO.setUser(userDTO);
        return userAuthenticationDTO;
    }

    public static UserDTO convertFromUser(User user) throws NullPointerException, IllegalArgumentException {
        UserDTO userDTO = new UserDTO();
        userDTO.setAccountEnabled(user.getAccountEnabled());
        userDTO.setAccountExpired(user.getAccountExpired());
        userDTO.setAccountLocked(user.getAccountLocked());
        userDTO.setCredentialsExpired(user.getCredentialsExpired());
        userDTO.setEmail(user.getEmail());
        userDTO.setId(user.getId());
        userDTO.setIsDeleted(user.getIsDeleted());
        userDTO.setUsername(user.getUsername());
        userDTO.setFirstName(user.getFirstName());
        userDTO.setLastName(user.getLastName());
        userDTO.setRoles(user.getUserAuthorities().stream().filter(userAuthority -> nonNull(userAuthority) && (userAuthority.getStatus() == ACTIVE)).map(userAuthority -> new AuthorityDTO(userAuthority.getAuthority().getId(), userAuthority.getAuthority().getAuthority(), userAuthority.getStatus())).collect(toList()));
        return userDTO;
    }

    public static JobDetailsCO convertFromJobDetails(JobDetail jobDetail, int countTriggers, User group) {
        return new JobDetailsCO(
                KeyGroupDescriptionDTO.getInstance(jobDetail.getKey().getName(), convertFromUser(group), jobDetail.getDescription()),
                findJobExecutorClassByValue(jobDetail.getJobClass()),
                jobDetail.getJobDataMap().getWrappedMap(),
                jobDetail.isDurable(),
                countTriggers > 0,
                jobDetail.requestsRecovery(),
                jobDetail.isConcurrentExectionDisallowed(),
                jobDetail.isPersistJobDataAfterExecution(),
                jobDetail.getJobDataMap().containsKey(CREATED_DATE) ? jobDetail.getJobDataMap().getLongValue(CREATED_DATE) : null,
                jobDetail.getJobDataMap().containsKey(UPDATED_DATE) ? jobDetail.getJobDataMap().getLongValue(UPDATED_DATE) : null
        );
    }

    public static TriggerDetailsCO convertFromTrigger(Trigger trigger, Trigger.TriggerState triggerState, User group) {
        TriggerDetailsCO triggerDetail = new TriggerDetailsCO(
                new KeyGroupDescriptionDTO(trigger.getKey().getName(), convertFromUser(group), trigger.getDescription()),
                trigger.getStartTime(),
                trigger.getNextFireTime(),
                trigger.getPreviousFireTime(),
                trigger.getEndTime(),
                trigger.getFinalFireTime(),
                trigger.getPriority(),
                triggerState.toString());
        if (trigger instanceof SimpleTrigger) {
            triggerDetail.setType(SIMPLE);
            TriggerDetailsCO.SimpleTriggerDetails simpleTriggerDetails = triggerDetail.new SimpleTriggerDetails();
            simpleTriggerDetails.setCountTriggered(((SimpleTrigger) trigger).getTimesTriggered());
            simpleTriggerDetails.setRepeatCount(((SimpleTrigger) trigger).getRepeatCount());
            simpleTriggerDetails.setRepeatInterval(((SimpleTrigger) trigger).getRepeatInterval());
        } else if (trigger instanceof CronTrigger) {
            triggerDetail.setType(CRON);
            TriggerDetailsCO.CronTriggerDetails cronTriggerDetails = triggerDetail.new CronTriggerDetails();
            cronTriggerDetails.setCronExpression(((CronTrigger) trigger).getCronExpression());
            cronTriggerDetails.setExpressionSummary(((CronTrigger) trigger).getExpressionSummary());
            cronTriggerDetails.setTimeZone(((CronTrigger) trigger).getTimeZone());
        }
        return triggerDetail;
    }
}
