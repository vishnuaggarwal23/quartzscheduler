package wrapper.quartz.scheduler.dto;

import lombok.*;
import org.springframework.util.Assert;

import java.util.Set;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class GroupDTO {
    private Long id;
    private String name;
    private UserDTO admin;
    private Set<UserDTO> members;

    public GroupDTO(String id) throws IllegalArgumentException {
        Assert.hasText(id, "User id is not available");
        this.id = Long.valueOf(id);
    }

    public GroupDTO(Long id) throws IllegalArgumentException {
        Assert.notNull(id, "User is not available");
        this.id = id;
    }
}
