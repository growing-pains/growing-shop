package com.example.config;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        JsonJacksonCodec codec = new JsonJacksonCodec(mapper);
        Config config = Config.fromYAML(configFile.getInputStream())
                .setCodec(codec);

        return Redisson.create(config);
    }
}
