package wrapper.quartz.scheduler.dto;

import lombok.*;
import org.apache.commons.lang3.StringUtils;

/**
 * The type Login dto.
 */
@Getter
@Setter
@ToString
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    private String username;
    private String password;

    /**
     * Is valid boolean.
     *
     * @return the boolean
     */
    public boolean isValid() {
        return !StringUtils.isAnyBlank(this.username, this.password);
    }
}