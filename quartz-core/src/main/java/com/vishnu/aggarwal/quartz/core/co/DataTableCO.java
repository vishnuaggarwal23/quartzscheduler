package com.vishnu.aggarwal.quartz.core.co;

import lombok.Getter;
import lombok.Setter;

/**
 * The type Data table co.
 */
@Setter
@Getter
public class DataTableCO {
    private int max = 10;
    private int offset = 0;
    private String sortBy = "id";
    private String orderBy = "desc";
}
