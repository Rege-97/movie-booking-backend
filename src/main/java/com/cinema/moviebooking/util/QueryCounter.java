package com.cinema.moviebooking.util;

public class QueryCounter {

    // 각 스레드별로 독립적인 쿼리 카운트를 저장
    private static final ThreadLocal<Integer> count = new ThreadLocal<>();

    public static void start() {
        count.set(0); // 요청 시작 시 카운터 초기화
    }

    public static void increase() {
        Integer currentCount = count.get();
        if (currentCount != null) {
            count.set(currentCount + 1); // 쿼리가 실행될 때마다 1 증가
        }
    }

    public static Integer getCount() {
        return count.get(); // 현재 요청의 쿼리 카운트 반환
    }

    public static void end() {
        count.remove(); // 요청 종료 시 ThreadLocal 데이터 제거 (메모리 누수 방지)
    }
}