package com.aiqin.mgs.order.api.config;

import org.springframework.boot.autoconfigure.data.redis.RedisProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisPassword;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import javax.annotation.Resource;

/**
 * AppConfig
 *
 * @author zhangtao
 * @createTime 2018/12/26 下午3:02
 * @description
 */
@Configuration
@EnableConfigurationProperties(RedisProperties.class)
public class RedisConfig {

    @Resource
    private RedisProperties redisProperties;

    @Bean
    public JedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration(redisProperties.getHost(), redisProperties.getPort());
        config.setPassword(RedisPassword.of(redisProperties.getPassword()));
        return new JedisConnectionFactory(config);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);
//        ObjectMapper om = new ObjectMapper();
//        om.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.ANY);
//        om.enableDefaultTyping(ObjectMapper.DefaultTyping.NON_FINAL);
//        jackson2JsonRedisSerializer.setObjectMapper(om);
//        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
//        template.setConnectionFactory(redisConnectionFactory);
//        template.setKeySerializer(jackson2JsonRedisSerializer);
//        template.setValueSerializer(jackson2JsonRedisSerializer);
//        template.setHashKeySerializer(jackson2JsonRedisSerializer);
//        template.setHashValueSerializer(jackson2JsonRedisSerializer);
//        template.afterPropertiesSet();
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        RedisSerializer<String> stringRedisSerializer = new StringRedisSerializer();
        template.setConnectionFactory(redisConnectionFactory);
        template.setKeySerializer(stringRedisSerializer);
        template.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.setHashKeySerializer(stringRedisSerializer);
        template.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));
        template.afterPropertiesSet();
        return template;
    }
}