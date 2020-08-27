package wrapper.quartz.scheduler.entity.jpa;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import wrapper.quartz.scheduler.util.LoggerUtility;

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
@Slf4j
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

    @Override
    public void setDeleted(boolean deleted) {
        LoggerUtility.warn(log, "QuartzGroup cannot be deleted");
        super.setDeleted(false);
    }
}
