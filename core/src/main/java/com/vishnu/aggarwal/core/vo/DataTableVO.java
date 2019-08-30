package com.vishnu.aggarwal.core.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

import static java.util.Objects.isNull;
import static org.springframework.util.CollectionUtils.isEmpty;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DataTableVO<T> {
    private Integer count;
    private Integer recordsTotal;
    private Integer recordsFiltered;
    private List<T> data;

    public DataTableVO<T> createInstance(Integer count, Integer recordsTotal, Integer recordsFiltered, List<T> data) {
        return isEmpty(data) ?
                new DataTableVO<T>(isNull(count) ? 0 : count, isNull(recordsTotal) ? 0 : recordsTotal, isNull(recordsFiltered) ? 0 : recordsFiltered, null) :
                new DataTableVO<T>(count, recordsTotal, recordsFiltered, data);
    }

    public DataTableVO<T> createInstance(List<T> data) {
        return isEmpty(data) ?
                new DataTableVO<T>(0, 0, 0, null) :
                new DataTableVO<T>(data.size(), data.size(), data.size(), data);
    }
}
