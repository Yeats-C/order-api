package com.aiqin.mgs.order.api.component;

import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 序列号生成dao
 */
@Component
public class SequenceRedisDao {

    @Resource
    private RedisTemplate redisTemplate;

    public Integer nextSequence(SequenceType type, String dateStr, Integer expireTime) {
        String key = this.getSequenceKey(type.getName(), dateStr);
        return incr(key, expireTime).intValue() + 1;
    }

    public Integer nextSequence(SequenceType type, String code, String dateStr, Integer expireTime) {
        String key = this.getSequenceKey(type.getName(), code, dateStr);
        return incr(key, expireTime).intValue() + 1;
    }

    public Integer nextSequence(SequenceType type) {
        String key = this.getSequenceKey(type.getName());
        return incr(key, -1, false).intValue() + 1;
    }

    private String getSequenceKey(String... params) {
        StringBuilder builder = new StringBuilder("sequence");
        for (String param : params) {
            builder.append(":").append(param);
        }
        return builder.toString();
    }

    public Long incr(String key, int expile) {
        return incr(key, expile, true);
    }


    private Long incr(String key, int liveTime, Boolean b) {
        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, redisTemplate.getConnectionFactory());
        Long increment = entityIdCounter.getAndIncrement();
        if ((null == increment || increment.longValue() == 0) && liveTime > 0 && b) {//初始设置过期时间
            entityIdCounter.expire(liveTime, TimeUnit.SECONDS);
        }
        return increment;
    }

    private String get(final String key) {
        String result = (String) redisTemplate.execute(new RedisCallback<String>() {
            @Override
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                RedisSerializer<String> serializer = redisTemplate.getStringSerializer();
                byte[] value = connection.get(serializer.serialize(key));
                return serializer.deserialize(value);
            }
        });
        return result;
    }
}
