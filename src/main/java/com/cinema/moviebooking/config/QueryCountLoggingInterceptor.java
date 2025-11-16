package com.cinema.moviebooking.config;

import com.cinema.moviebooking.util.QueryCounter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Slf4j
@Component
public class QueryCountLoggingInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 1. 요청 시작 시: 쿼리 카운터 초기화
        QueryCounter.start();
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        // 4. 요청 완료 시: 쿼리 카운트 가져오기 및 로깅
        Integer queryCount = QueryCounter.getCount();

        if (queryCount != null) {
            log.info("Request URI: {} [{}], Query Count: {}",
                    request.getRequestURI(),
                    request.getMethod(),
                    queryCount);

            // 5. ThreadLocal 정리
            QueryCounter.end();
        }
    }
}