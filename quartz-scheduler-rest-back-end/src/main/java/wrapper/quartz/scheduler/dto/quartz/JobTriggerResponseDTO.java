package wrapper.quartz.scheduler.dto.quartz;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.Date;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class JobTriggerResponseDTO {
    private KeyGroupDescriptionDTO trigger;
    private KeyGroupDescriptionDTO job;
    private HttpStatus responseCode;
    private String responseHeader;
    private Object responseBody;
    private Date fireTime;
}
