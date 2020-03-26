//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package com.aiqin.mgs.order.api.service;

import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.DataType;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service("redisService")
public class RedisService {

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    public RedisService() {
    }

    public boolean expire(String key, long time) {
        try {
            if (time > 0L) {
                this.redisTemplate.expire(key, time, TimeUnit.SECONDS);
            }

            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public long getExpire(String key) {
        return this.redisTemplate.getExpire(key, TimeUnit.SECONDS);
    }

    public boolean hasKey(String key) {
        try {
            return this.redisTemplate.hasKey(key);
        } catch (Exception var3) {
            var3.printStackTrace();
            return false;
        }
    }

    public void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                this.redisTemplate.delete(key[0]);
            } else {
                this.redisTemplate.delete(CollectionUtils.arrayToList(key));
            }
        }

    }

    public Object get(String key) {
        return key == null ? null : this.redisTemplate.opsForValue().get(key);
    }

    public boolean set(String key, Object value) {
        try {
            this.redisTemplate.opsForValue().set(key, value);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public boolean setSeconds(String key, Object value, long time) {
        if (time == 0L) {
            return false;
        } else {
            try {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
                return true;
            } catch (Exception var6) {
                var6.printStackTrace();
                return false;
            }
        }
    }

    public boolean setMinutes(String key, Object value, long time) {
        if (time == 0L) {
            return false;
        } else {
            try {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.MINUTES);
                return true;
            } catch (Exception var6) {
                var6.printStackTrace();
                return false;
            }
        }
    }

    public boolean setHours(String key, Object value, long time) {
        if (time == 0L) {
            return false;
        } else {
            try {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.HOURS);
                return true;
            } catch (Exception var6) {
                var6.printStackTrace();
                return false;
            }
        }
    }

    public boolean setDays(String key, Object value, long time) {
        if (time == 0L) {
            return false;
        } else {
            try {
                this.redisTemplate.opsForValue().set(key, value, time, TimeUnit.DAYS);
                return true;
            } catch (Exception var6) {
                var6.printStackTrace();
                return false;
            }
        }
    }

    public long incr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递增因子必须大于0");
        } else {
            return this.redisTemplate.opsForValue().increment(key, delta);
        }
    }

    public long decr(String key, long delta) {
        if (delta < 0L) {
            throw new RuntimeException("递减因子必须大于0");
        } else {
            return this.redisTemplate.opsForValue().increment(key, -delta);
        }
    }

    public Object hget(String key, String item) {
        return this.redisTemplate.opsForHash().get(key, item);
    }

    public Map<Object, Object> hmget(String key) {
        return this.redisTemplate.opsForHash().entries(key);
    }

    public boolean hmset(String key, Map<String, Object> map) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public boolean hmset(String key, Map<String, Object> map, long time) {
        try {
            this.redisTemplate.opsForHash().putAll(key, map);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean hset(String key, String item, Object value) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            return true;
        } catch (Exception var5) {
            var5.printStackTrace();
            return false;
        }
    }

    public boolean hset(String key, String item, Object value, long time) {
        try {
            this.redisTemplate.opsForHash().put(key, item, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var7) {
            var7.printStackTrace();
            return false;
        }
    }

    public void hdel(String key, Object... item) {
        this.redisTemplate.opsForHash().delete(key, item);
    }

    public boolean hHasKey(String key, String item) {
        return this.redisTemplate.opsForHash().hasKey(key, item);
    }

    public double hincr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, by);
    }

    public double hdecr(String key, String item, double by) {
        return this.redisTemplate.opsForHash().increment(key, item, -by);
    }

    public Set<Object> sGet(String key) {
        try {
            return this.redisTemplate.opsForSet().members(key);
        } catch (Exception var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public boolean sHasKey(String key, Object value) {
        try {
            return this.redisTemplate.opsForSet().isMember(key, value);
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public long sSet(String key, Object... values) {
        try {
            return this.redisTemplate.opsForSet().add(key, values);
        } catch (Exception var4) {
            var4.printStackTrace();
            return 0L;
        }
    }

    public long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = this.redisTemplate.opsForSet().add(key, values);
            if (time > 0L) {
                this.expire(key, time);
            }

            return count;
        } catch (Exception var6) {
            var6.printStackTrace();
            return 0L;
        }
    }

    public long sGetSetSize(String key) {
        try {
            return this.redisTemplate.opsForSet().size(key);
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public long setRemove(String key, Object... values) {
        try {
            Long count = this.redisTemplate.opsForSet().remove(key, values);
            return count;
        } catch (Exception var4) {
            var4.printStackTrace();
            return 0L;
        }
    }

    public List<Object> lGet(String key, long start, long end) {
        try {
            return this.redisTemplate.opsForList().range(key, start, end);
        } catch (Exception var7) {
            var7.printStackTrace();
            return null;
        }
    }

    public long lGetListSize(String key) {
        try {
            return this.redisTemplate.opsForList().size(key);
        } catch (Exception var3) {
            var3.printStackTrace();
            return 0L;
        }
    }

    public Object lGetIndex(String key, long index) {
        try {
            return this.redisTemplate.opsForList().index(key, index);
        } catch (Exception var5) {
            var5.printStackTrace();
            return null;
        }
    }

    public boolean lSet(String key, Object value) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, Object value, long time) {
        try {
            this.redisTemplate.opsForList().rightPush(key, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, List<Object> value) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            return true;
        } catch (Exception var4) {
            var4.printStackTrace();
            return false;
        }
    }

    public boolean lSet(String key, List<Object> value, long time) {
        try {
            this.redisTemplate.opsForList().rightPushAll(key, value);
            if (time > 0L) {
                this.expire(key, time);
            }

            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public boolean lUpdateIndex(String key, long index, Object value) {
        try {
            this.redisTemplate.opsForList().set(key, index, value);
            return true;
        } catch (Exception var6) {
            var6.printStackTrace();
            return false;
        }
    }

    public long lRemove(String key, long count, Object value) {
        try {
            Long remove = this.redisTemplate.opsForList().remove(key, count, value);
            return remove;
        } catch (Exception var6) {
            var6.printStackTrace();
            return 0L;
        }
    }

    public Set<String> keys(String pattern) {
        return this.redisTemplate.keys(pattern);
    }

    public List hvals(String key) {
        return this.redisTemplate.opsForHash().values(key);
    }

    public DataType type(String key) {
        return this.redisTemplate.type(key);
    }

    public Object flushDB() {
        return this.redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushDb();
                return "ok";
            }
        });
    }

    public Object flushAllDB() {
        return this.redisTemplate.execute(new RedisCallback() {
            public String doInRedis(RedisConnection connection) throws DataAccessException {
                connection.flushAll();
                return "ok";
            }
        });
    }

    public Properties getRedisServerInfo() {
        Properties info = this.redisTemplate.getConnectionFactory().getConnection().info("server");
        return info;
    }

    public Properties getRedisMemoryInfo() {
        Properties info = this.redisTemplate.getConnectionFactory().getConnection().info("memory");
        return info;
    }

    public Properties getRedisClientsInfo() {
        Properties info = this.redisTemplate.getConnectionFactory().getConnection().info("clients");
        return info;
    }

    public Properties getRedisPersistenceInfo() {
        Properties info = this.redisTemplate.getConnectionFactory().getConnection().info("persistence");
        return info;
    }

    public Properties getRedisStatsInfo() {
        Properties info = this.redisTemplate.getConnectionFactory().getConnection().info("stats");
        return info;
    }

    public Properties getRedisReplicationInfo() {
        Properties info = this.redisTemplate.getConnectionFactory().getConnection().info("replication");
        return info;
    }

    public Properties getRedisCPUInfo() {
        Properties info = this.redisTemplate.getConnectionFactory().getConnection().info("CPU");
        return info;
    }

    public Properties getRedisClusterInfo() {
        Properties info = this.redisTemplate.getConnectionFactory().getConnection().info("cluster");
        return info;
    }

    public Properties getRedisKeyspaceInfo() {
        Properties info = this.redisTemplate.getConnectionFactory().getConnection().info("keyspace");
        return info;
    }
}
