package com.vishnu.aggarwal.core.co;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Data table co.
 */
@Setter
@Getter
public class DataTableCO {
    private Integer max = 10;
    private Integer offset = 0;
    private String sortBy = "id";
    private String orderBy = "desc";
}
