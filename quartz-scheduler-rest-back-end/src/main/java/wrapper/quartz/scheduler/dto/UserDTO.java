package wrapper.quartz.scheduler.dto;

import lombok.*;
import org.springframework.util.Assert;

import java.util.Set;

/**
 * The type User dto.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String username;
    private String email;
    private String password;
    private Boolean accountExpired = false;
    private Boolean accountLocked = false;
    private Boolean credentialsExpired = false;
    private Boolean accountEnabled = true;
    private Boolean deleted = false;
    private String firstName;
    private String lastName;
    private Set<AuthorityDTO> roles;
    private GroupDTO group;

    /**
     * Instantiates a new User dto.
     *
     * @param id the id
     * @throws IllegalArgumentException the illegal argument exception
     */
    public UserDTO(String id) throws IllegalArgumentException {
        Assert.hasText(id, "User id is not available");
        this.id = Long.valueOf(id);
    }

    /**
     * Instantiates a new User dto.
     *
     * @param id the id
     * @throws IllegalArgumentException the illegal argument exception
     */
    public UserDTO(Long id) throws IllegalArgumentException {
        Assert.notNull(id, "User is not available");
        this.id = id;
    }
}