package wrapper.quartz.scheduler.entity.jpa;

import lombok.*;

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
public class QuartzGroup extends BaseEntity<Long> {
    private static final long serialVersionUID = 4788225726117978147L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    private String name;

    @EqualsAndHashCode.Exclude
    @OneToMany(mappedBy = "quartzGroup", fetch = FetchType.EAGER)
    private Set<User> users;
}
