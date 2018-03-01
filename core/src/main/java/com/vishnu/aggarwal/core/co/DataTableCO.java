package com.vishnu.aggarwal.core.co;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Data table co.
 */
@Setter
@Getter
public class DataTableCO {
    private Integer max;
    private Integer offset;
    private String sortBy = "id";
    private String orderBy = "desc";
}
