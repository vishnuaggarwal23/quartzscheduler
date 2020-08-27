package wrapper.quartz.scheduler.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * The enum Authority enum.
 */
@Getter
@AllArgsConstructor
public enum AuthorityEnum {
    /**
     * The Application admin.
     */
    APPLICATION_ADMIN("Application Admin", "Application Admin Role"),
    /**
     * The Group admin.
     */
    GROUP_ADMIN("Group Admin", "Group Admin Role"),
    /**
     * The Group user.
     */
    GROUP_USER("Group User", "Group User Role"),
    /**
     * The Application user.
     */
    APPLICATION_USER("Application User", "Application User Role"),
    /**
     * The Job.
     */
    JOB("Job", "Job Role"),
    /**
     * The Trigger.
     */
    TRIGGER("Trigger", "Trigger Role");

    private final String displayName;
    private final String description;

    /**
     * Gets authorities.
     *
     * @return the authorities
     */
    public static List<AuthorityEnum> getAuthorities() {
        return Collections.unmodifiableList(Arrays.asList(values()));
    }
}