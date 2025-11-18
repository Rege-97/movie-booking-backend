package com.cinema.moviebooking.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.atomic.AtomicBoolean;

@Configuration
public class SchedulerStateConfig {

    @Bean
    public AtomicBoolean schedulerReadyFlag() {
        return new AtomicBoolean(false);
    }
}
