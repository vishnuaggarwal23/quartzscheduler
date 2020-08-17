package wrapper.quartz.scheduler.dto;

import lombok.*;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class AuthorityDTO {
    private Long id;
    private String authority;
}