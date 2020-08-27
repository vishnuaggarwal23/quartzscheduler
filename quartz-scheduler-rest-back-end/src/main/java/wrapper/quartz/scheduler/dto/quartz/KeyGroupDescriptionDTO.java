package wrapper.quartz.scheduler.dto.quartz;

import lombok.*;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import wrapper.quartz.scheduler.dto.GroupDTO;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class KeyGroupDescriptionDTO {
    private String key;
    private GroupDTO group;
    private String description;

    public KeyGroupDescriptionDTO(JobDetail jobDetail) {
        this.key = jobDetail.getKey().getName();
        this.group = new GroupDTO(jobDetail.getKey().getGroup());
        this.description = jobDetail.getDescription();
    }

    public KeyGroupDescriptionDTO(Trigger trigger) {
        this.key = trigger.getKey().getName();
        this.group = new GroupDTO(trigger.getKey().getGroup());
        this.description = trigger.getDescription();
    }
}
