package com.vishnu.aggarwal.core.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

/**
 * The type Data table vo.
 *
 * @param <T> the type parameter
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DataTableVO<T> {
    private Integer count;
    private Integer recordsTotal;
    private Integer recordsFiltered;
    private List<T> data;
}
