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
    ROLE_APPLICATION_ADMIN("Application Admin", "Application Admin Role"),
    /**
     * The Group admin.
     */
    ROLE_GROUP_ADMIN("Group Admin", "Group Admin Role"),
    /**
     * The Group user.
     */
    ROLE_GROUP_USER("Group User", "Group User Role"),
    /**
     * The Application user.
     */
    ROLE_APPLICATION_USER("Application User", "Application User Role"),
    /**
     * The Job.
     */
    ROLE_JOB("Job", "Job Role"),
    /**
     * The Trigger.
     */
    ROLE_TRIGGER("Trigger", "Trigger Role");

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