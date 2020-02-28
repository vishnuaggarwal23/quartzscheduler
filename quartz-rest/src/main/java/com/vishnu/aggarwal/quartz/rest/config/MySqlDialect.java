package com.vishnu.aggarwal.quartz.rest.config;

import org.hibernate.dialect.MySQL57Dialect;

/**
 * The type My sql dialect.
 */
/*
Created by vishnu on 24/4/18 1:04 PM
*/
public class MySqlDialect extends MySQL57Dialect {
    @Override
    public String getTableTypeString() {
        return " ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin";
    }
}
