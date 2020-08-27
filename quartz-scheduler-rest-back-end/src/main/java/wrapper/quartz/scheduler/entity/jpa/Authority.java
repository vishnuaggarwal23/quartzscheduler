package wrapper.quartz.scheduler.entity.jpa;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.GrantedAuthority;
import wrapper.quartz.scheduler.enums.AuthorityEnum;
import wrapper.quartz.scheduler.util.LoggerUtility;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * The type Authority.
 */
@Entity
@Table
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@Slf4j
public class Authority extends BaseEntity<Long> implements GrantedAuthority {
    private static final long serialVersionUID = 6607469015526449399L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private AuthorityEnum name;

    @ManyToMany(mappedBy = "authorities", fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<User> users;

    @Override
    @Transient
    public String getAuthority() {
        return name.name();
    }

    @Override
    public void setDeleted(boolean deleted) {
        LoggerUtility.warn(log, "Authority cannot be deleted");
        super.setDeleted(false);
    }
}