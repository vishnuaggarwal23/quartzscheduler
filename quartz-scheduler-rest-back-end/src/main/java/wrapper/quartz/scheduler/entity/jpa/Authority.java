package wrapper.quartz.scheduler.entity.jpa;

import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import wrapper.quartz.scheduler.enums.AuthorityEnum;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@ToString(callSuper = true)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
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
}