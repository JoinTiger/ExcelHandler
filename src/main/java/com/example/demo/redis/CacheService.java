package com.example.demo.redis;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.support.atomic.RedisAtomicLong;
import org.springframework.stereotype.Service;

@Service
public class CacheService { 

		 //这里因为有其他的template,所以名字起得不好看
		@Autowired
     	RedisTemplate<String, Object> ObjectRedisTemplate;
     	 
		public Long getIncrementNum(String key) {
	        // 不存在准备创建 键值对
	        RedisAtomicLong entityIdCounter = new RedisAtomicLong(key, ObjectRedisTemplate.getConnectionFactory());
	        Long counter = entityIdCounter.incrementAndGet();
	        
	        if ((null == counter || counter.longValue() == 1)) {// 初始设置过期时间
	            
	        	int remainSeconds = getRemainSecondsOneDay(new Date());
	            entityIdCounter.expire(remainSeconds, TimeUnit.SECONDS);// 单位
	        }
	        return counter;
		}
		
		public static Integer getRemainSecondsOneDay(Date currentDate) {
	        LocalDateTime midnight = LocalDateTime.ofInstant(currentDate.toInstant(),
	                ZoneId.systemDefault()).plusDays(1).withHour(0).withMinute(0)
	                .withSecond(0).withNano(0);
	        LocalDateTime currentDateTime = LocalDateTime.ofInstant(currentDate.toInstant(),
	                ZoneId.systemDefault());
	        long seconds = ChronoUnit.SECONDS.between(currentDateTime, midnight);
	        return (int) seconds;
	    }
		
		
}
