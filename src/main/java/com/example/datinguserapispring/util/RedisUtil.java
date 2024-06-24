package com.example.datinguserapispring.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

@Component
public class RedisUtil<T> {
	
    private RedisTemplate<String, T> redisTemplate;
    private ValueOperations<String, T> valueOperations;

    @Autowired
    public RedisUtil(RedisTemplate<String, T> redisTemplate) {
        this.redisTemplate = redisTemplate;
        this.valueOperations = redisTemplate.opsForValue();
    }

    public void putValue(String key, T value) {
        valueOperations.set(key, value);
    }

    public T getValue(String key) {
        return valueOperations.get(key);
    }

    public void setExpire(String key, long timeout, TimeUnit unit) {
        redisTemplate.expire(key, timeout, unit);
    }

}
