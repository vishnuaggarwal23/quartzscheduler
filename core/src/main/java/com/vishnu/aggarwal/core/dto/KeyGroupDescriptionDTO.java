package com.vishnu.aggarwal.core.dto;

import com.vishnu.aggarwal.core.co.DataTableCO;
import lombok.*;

import static org.apache.commons.lang3.StringUtils.EMPTY;

/**
 * The type Key group name dto.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class KeyGroupDescriptionDTO extends DataTableCO {
    private String key;
    private UserDTO group;
    private String description;

    public static KeyGroupDescriptionDTO getInstance(String key, UserDTO group, String description) {
        return new KeyGroupDescriptionDTO(key, group, description);
    }

    public static KeyGroupDescriptionDTO getInstance() {
        return new KeyGroupDescriptionDTO(EMPTY, null, EMPTY);
    }
}
