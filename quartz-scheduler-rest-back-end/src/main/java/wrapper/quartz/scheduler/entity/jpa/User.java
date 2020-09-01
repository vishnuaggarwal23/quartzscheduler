package wrapper.quartz.scheduler.entity.jpa;

import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Set;

@Entity
@Table
@Getter
@Setter
@NoArgsConstructor
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class User extends BaseEntity<Long> implements UserDetails {
    private static final long serialVersionUID = 8422626226848372751L;

    @Id
    @NotNull
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotNull
    @NotEmpty
    private String username;

    @NotNull
    @NotEmpty
    @ToString.Exclude
    @Size(min = 8)
    private String password;

    @NotNull
    @Email
    @NotEmpty
    private String email;

    private String firstName;
    private String middleName;
    private String lastName;

    private boolean accountExpired = false;
    private boolean accountEnabled = false;
    private boolean accountLocked = false;
    private boolean credentialsExpired = false;

    @ManyToOne(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private QuartzGroup quartzGroup;

    @ManyToMany(fetch = FetchType.EAGER)
    @EqualsAndHashCode.Exclude
    private Set<Authority> authorities;

    @Override
    @Transient
    public boolean isAccountNonExpired() {
        return !this.accountExpired;
    }

    @Override
    @Transient
    public boolean isAccountNonLocked() {
        return !this.accountLocked;
    }

    @Override
    @Transient
    public boolean isCredentialsNonExpired() {
        return !this.credentialsExpired;
    }

    @Override
    @Transient
    public boolean isEnabled() {
        return !this.accountEnabled;
    }

    @Transient
    public String getFullName() {
        return String.format("%s %s %s", this.firstName, this.middleName, this.lastName);
    }
}