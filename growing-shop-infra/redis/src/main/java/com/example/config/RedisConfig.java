package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.codec.JsonJacksonCodec;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

import java.io.IOException;

@Configuration
public class RedisConfig {

    @Bean
    public RedissonClient redissonClient(@Value("classpath:/${redis.config}") Resource configFile) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        // FIXME - jpa proxy 를 다른곳에서 사용할 때 deserialize 예외가 발생함. 해결 또는 더 좋은 방법 찾아보기

        JsonJacksonCodec codec = new JsonJacksonCodec(mapper);
        Config config = Config.fromYAML(configFile.getInputStream())
                .setCodec(codec);

        return Redisson.create(config);
    }
}
