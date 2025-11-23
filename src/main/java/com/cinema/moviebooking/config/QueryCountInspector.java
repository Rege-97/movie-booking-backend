package com.cinema.moviebooking.config;

import com.cinema.moviebooking.util.QueryCounter;
import org.hibernate.resource.jdbc.spi.StatementInspector;

public class QueryCountInspector implements StatementInspector {

    @Override
    public String inspect(String sql) {
        // 쿼리가 실행될 때마다 카운터를 증가
        QueryCounter.increase();
        return sql; // 원본 SQL 반환
    }
}