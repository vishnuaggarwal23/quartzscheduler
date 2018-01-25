package com.vishnu.aggarwal.core.vo;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class DataTableVO<T> {
    private Long count;
    private Long recordsTotal;
    private Long recordsFiltered;
    private List<T> data;
}
