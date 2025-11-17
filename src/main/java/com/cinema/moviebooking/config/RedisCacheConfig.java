package com.cinema.moviebooking.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.jsontype.BasicPolymorphicTypeValidator;
import com.fasterxml.jackson.databind.jsontype.PolymorphicTypeValidator;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
public class RedisCacheConfig {

    private ObjectMapper createRedisObjectMapper() {
        PolymorphicTypeValidator ptv = BasicPolymorphicTypeValidator.builder()
                .allowIfBaseType(Object.class)
                .build();

        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.activateDefaultTyping(ptv, ObjectMapper.DefaultTyping.NON_FINAL);
        return mapper;
    }

    // 캐시 설정을 생성하는 헬퍼 메서드
    private RedisCacheConfiguration createCacheConfiguration(Duration ttl, ObjectMapper objectMapper) {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(ttl)
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(
                        new GenericJackson2JsonRedisSerializer(objectMapper)
                ));
    }

    @Bean
    public CacheManager cacheManager(RedisConnectionFactory redisConnectionFactory) {

        ObjectMapper redisObjectMapper = createRedisObjectMapper();
        // 기본 캐시 설정
        RedisCacheConfiguration defaultConf = createCacheConfiguration(Duration.ofHours(1), redisObjectMapper);

        // 상영 스케쥴 설정
        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();
        cacheConfigurations.put("cinemaScreening", createCacheConfiguration(Duration.ofMinutes(10), redisObjectMapper));

        return RedisCacheManager.builder(redisConnectionFactory)
                .cacheDefaults(defaultConf)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
