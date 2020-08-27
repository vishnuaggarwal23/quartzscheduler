package wrapper.quartz.scheduler.entity.jpa;

import lombok.*;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToOne;
import javax.persistence.Version;
import java.io.Serializable;

/**
 * The type Base entity.
 *
 * @param <ID> the type parameter
 */
@Getter
@Setter
@MappedSuperclass
@ToString
@EntityListeners(AuditingEntityListener.class)
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public abstract class BaseEntity<ID> implements Serializable {
    private static final long serialVersionUID = -8742854812854445762L;

    private boolean deleted = false;

    @Version
    private long version;

    @CreatedDate
    private Long dateCreated;

    @LastModifiedDate
    private Long dateUpdated;

    @CreatedBy
    @OneToOne
    @EqualsAndHashCode.Exclude
    private User createdBy;

    @LastModifiedBy
    @OneToOne
    @EqualsAndHashCode.Exclude
    private User updatedBy;

    /**
     * Gets id.
     *
     * @return the id
     */
    protected abstract ID getId();
}
