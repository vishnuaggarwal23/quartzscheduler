package com.vishnu.aggarwal.core.dto;

import lombok.*;

/**
 * The type Key group name dto.
 */
@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class KeyGroupDescriptionDTO {
    private String keyName;
    private String groupName;
    private String description;
}
