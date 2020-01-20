package info.xiaomo.redis.util;

import info.xiaomo.redis.util.enums.RedisChannel;
import info.xiaomo.redis.util.key.RedisKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.redis.core.Cursor;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ScanOptions;
import org.springframework.data.redis.core.SetOperations;
import org.springframework.data.redis.core.ZSetOperations;
import org.springframework.util.CollectionUtils;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @Name RedisUtil
 * @Author 900045
 * @Created by 2020/1/20 0020
 */
public final class RedisUtil<T> {

	private Logger logger = LoggerFactory.getLogger(this.getClass());

	private static final Random RANDOM = new Random();

	private RedisTemplate<String, T> redisTemplate;

	private HashOperations<String, String, T> hashTemplate;
	private SetOperations<String, T> setTemplate;
	private ZSetOperations<String, T> zSetTemplate;

	public RedisUtil(RedisTemplate<String, T> redisTemplate) {
		if (redisTemplate == null) {
			throw new RuntimeException("RedisUtil<Object>init fail,  redisTemplate is null!");
		}
		this.redisTemplate = redisTemplate;
		this.hashTemplate = this.redisTemplate.opsForHash();
		this.setTemplate = redisTemplate.opsForSet();
		this.zSetTemplate = redisTemplate.opsForZSet();
	}

	/**
	 * 相当于 setNX 设置过期
	 * @param redisKey
	 * @param value
	 * @param time
	 * @param timeUnit
	 * @return
	 */
	public boolean setIfAbsent(RedisKey redisKey, T value , long time , TimeUnit timeUnit) {
		return this.redisTemplate.opsForValue().setIfAbsent(redisKey.getKey(),value,time,timeUnit);
	}

	/**
	 * 超时设置
	 *
	 * @param redisKey
	 * @param date
	 * @return
	 */
	public boolean expire(RedisKey redisKey, Date date) {
		Assert.notNull(redisKey);
		Assert.notNull(date);
		Boolean flag = this.redisTemplate.expireAt(redisKey.getKey(), date);
		return flag != null && flag;
	}

	/**
	 * 超时设置
	 *
	 * @param redisKey
	 * @param timeout
	 * @param timeUnit
	 * @return
	 */
	public boolean expire(RedisKey redisKey, long timeout, TimeUnit timeUnit) {
		Assert.notNull(redisKey);
		Assert.notNull(timeUnit);
		Boolean flag = this.redisTemplate.expire(redisKey.getKey(), timeout, timeUnit);
		return flag != null && flag;
	}

	/**
	 * 递增   步进值为1
	 *
	 * @param redisKey
	 * @return
	 */
	public Long increment(RedisKey redisKey) {
		Assert.notNull(redisKey);
		return increment(redisKey.getKey(), 1L);
	}



	/**
	 * 递增
	 *
	 * @param redisKey
	 * @param delta    步进值
	 * @return
	 */
	private Long increment(String redisKey, long delta) {
		Assert.notNull(redisKey);
		return redisTemplate.opsForValue().increment(redisKey, delta);
	}


	/**
	 * 递增
	 *
	 * @param redisKey
	 * @param delta
	 * @return
	 */
	public Long increment(RedisKey redisKey, long delta) {
		Assert.notNull(redisKey);
		return increment(redisKey.getKey(), delta);
	}

	public boolean exists(RedisKey key) {
		Boolean flag = this.redisTemplate.hasKey(key.getKey());
		return flag != null && flag;
	}

	/**
	 *  HASH  累加
	 *
	 * @param redisKey
	 * @param key
	 * @param init    累加值
	 * @return
	 */

	private int hashIncrement(String redisKey, String key, int init) {
		Assert.notNull(redisKey);
		Assert.notEmpty(key);
		return hashTemplate.increment(redisKey, key, init).intValue();
	}

	/**
	 * 获取hash的所有key
	 *
	 * @param redisKey
	 * @return
	 */
	public Set<String> hashKeys(String redisKey) {
		Assert.notNull(redisKey);
		return hashTemplate.keys(redisKey);
	}

	/**
	 * 获取hash的所有key
	 *
	 * @param redisKey
	 * @return
	 */
	public Long hashSize(String redisKey) {
		Assert.notNull(redisKey);
		return hashTemplate.size(redisKey);
	}

	/**
	 * 获取累加的值
	 *    注意： 此方法只获取不会累加
	 * @param redisKey
	 * @param key
	 * @return
	 */
	public int hashIncrementValue(RedisKey redisKey, String key) {
		return hashIncrement(redisKey.getKey(), key, 0);
	}


	public int hashIncrement(RedisKey redisKey, String key, Integer init) {
		Assert.notNull(redisKey);
		Assert.notEmpty(key);
		return hashTemplate.increment(redisKey.getKey(), key, init).intValue();
	}

	public int hashIncrement(RedisKey redisKey, String key, Long init) {
		Assert.notNull(redisKey);
		Assert.notEmpty(key);
		return hashTemplate.increment(redisKey.getKey(), key, init).intValue();
	}

	public Cursor<Map.Entry<String, T>> hashScan(RedisKey redisKey) {
		return hashTemplate.scan(redisKey.getKey(),
				ScanOptions.scanOptions().build());
	}

	public boolean hashHaskey(RedisKey redisKey, Object key) {
		Assert.notNull(redisKey);
		Assert.notNull(key);
		return hashTemplate.hasKey(redisKey.getKey(), Objects.toString(key));
	}





	public void hashDelete(String redisKey, String key) {
		Assert.notNull(redisKey);
		Assert.notEmpty(key);
		hashTemplate.delete(redisKey, key);
	}

	public <HK> void hashDelete(RedisKey redisKey, HK key) {
		hashDelete(redisKey.getKey(), key.toString());
	}

	public void hashDelete(RedisKey redisKey, Object... keys) {
		Assert.notNull(redisKey);
		if (keys != null && keys.length != 0) {
			hashTemplate.delete(redisKey.getKey(), keys);
		}
	}
	public Map<String, T> hash(RedisKey redisKey) {
		Assert.notNull(redisKey);
		return hashTemplate.entries(redisKey.getKey());
	}

	public List<T> hashValues(RedisKey redisKey) {
		Assert.notNull(redisKey);
		return hashTemplate.values(redisKey.getKey());
	}


	public <HK> T hash(RedisKey redisKey, HK key) {
		Assert.notNull(redisKey);
		Assert.notNull(key);
		return hashTemplate.get(redisKey.getKey(), key.toString());
	}

	public List<T> hashMultiGet(RedisKey redisKey, Collection<String> keys) {
		Assert.notNull(redisKey);
		Assert.notNull(keys);
		return hashTemplate.multiGet(redisKey.getKey(), keys);
	}

	public void hashAddAll(RedisKey redisKey, Map<String, T> map) {
		Assert.notNull(redisKey);
		hashTemplate.putAll(redisKey.getKey(), map);
	}

	public <HK> void hash(RedisKey redisKey, HK key, T value) {
		Assert.notNull(redisKey);
		Assert.notNull(key);
		hashTemplate.put(redisKey.getKey(), key.toString(), value);
	}

	public Long getExpire(RedisKey redisKey, TimeUnit timeUnit) {
		Assert.notNull(redisKey);
		Assert.notNull(timeUnit);
		return redisTemplate.getExpire(redisKey.getKey(), timeUnit);
	}


	// redis 消息  ---------------------------------------------------------- START ------------------------------------

	/**
	 * redis 消息发送
	 *
	 * @param channel
	 * @param message
	 */
	public void sendMsg(RedisChannel channel, Object message) {
		Assert.notNull(channel, "redis sendMsg  channel is null");
		Assert.notNull(message, "redis sendMsg  message is null");
		logger.error("redis-sendMsg : {} -> {} ", channel, message);
		redisTemplate.convertAndSend(channel.name(), message);
	}

	//  redis 消息  ---------------------------------------------------------- END ------------------------------------

	public Boolean hsetExists(RedisKey redisKey, T value) {
		Assert.notNull(redisKey);
		return setTemplate.isMember(redisKey.getKey(), value);
	}



	public Long hsetDelete(RedisKey redisKey, T value) {
		Assert.notNull(redisKey);
		return setTemplate.remove(redisKey.getKey(), value);
	}

	public T hset(RedisKey redisKey) {
		Assert.notNull(redisKey);
		return setTemplate.pop(redisKey.getKey());
	}



	public long zsetSize(RedisKey redisKey) {
		Assert.notNull(redisKey);
		return zSetTemplate.size(redisKey.getKey());
	}

	/**
	 *    获取指定分数里的第一个
	 *
	 * @param redisKey
	 * @param min
	 * @param max
	 * @return
	 */
	public T zsetOne(RedisKey redisKey, double min, double max) {
		//  zSetTemplate.rangeByScore(key, min, max)
		Set<T> range = zSetTemplate.rangeByScore(redisKey.getKey(), min, max, 0, 1);
		if (range != null && range.iterator().hasNext()) {
			return range.iterator().next();
		}
		return null;
	}

	public Set<T> zsetRange(RedisKey redisKey, double min, double max) {
		//  zSetTemplate.rangeByScore(key, min, max)
		return zSetTemplate.rangeByScore(redisKey.getKey(), min, max);
	}

	public Set<T> zsetRange(RedisKey redisKey, double min, double max, long length) {
		//  zSetTemplate.rangeByScore(key, min, max)
		return zSetTemplate.rangeByScore(redisKey.getKey(), min, max, 0, length);
	}

	public long zsetRemove(RedisKey redisKey, Object... objects) {
		Assert.notNull(redisKey);

		return zSetTemplate.remove(redisKey.getKey(), objects);
	}

	public boolean zsetAdd(RedisKey redisKey, T value, double score) {
		Assert.notNull(redisKey);
		return zSetTemplate.add(redisKey.getKey(), value, score);
	}


	public Set<T> zsetReverseRange(RedisKey redisKey, long start, long end) {
		Assert.notNull(redisKey);
		return zSetTemplate.reverseRange(redisKey.getKey(), start, end);
	}



	/**
	 * 队列添加
	 *
	 * @param value
	 */
	public Long leftPush(RedisKey redisKey, T value) {
		return redisTemplate.opsForList().leftPush(redisKey.getKey(), value);
	}

	public Long rightPush(RedisKey redisKey, T value) {
		return redisTemplate.opsForList().rightPush(redisKey.getKey(), value);
	}
	/**
	 * 队列取出
	 */
	public T rightPop(RedisKey redisKey) {
		return redisTemplate.opsForList().rightPop(redisKey.getKey());
	}

	/**
	 * 取出指定索引的值
	 *
	 * @param redisKey
	 * @param index
	 * @return
	 */
	public T listIndex(RedisKey redisKey, long index) {
		return listIndex(redisKey.getKey(), index);
	}

	public T listIndex(String key, long index) {
		return redisTemplate.opsForList().index(key, index);
	}

	/**
	 * 设置 key 不超时
	 * @param redisKey
	 * @param value
	 */
	public void setNotTimeOutKey(RedisKey redisKey, T value){
		redisTemplate.opsForValue().set(redisKey.getKey(), value);
	}

	/**
	 *
	 *
	 * @param key
	 * @param value
	 * @param timeout    超时时间
	 * @param timeUnit   超时时间单位
	 */
	public void set(RedisKey key, T value, long timeout, TimeUnit timeUnit) {
		redisTemplate.opsForValue().set(key.getKey(), value, getTimeout(timeout), timeUnit);
	}

	/**
	 * 获取
	 *
	 * @param key
	 * @return
	 */
	public T get(String key) {
		Assert.notNull(key);
		return redisTemplate.opsForValue().get(key);
	}

	/**
	 * 获取
	 *
	 * @param key
	 * @return
	 */
	public T get(RedisKey key) {
		Assert.notNull(key);
		return get(key.getKey());
	}


	public Boolean delete(String key) {
		Assert.notEmpty(key);
		return redisTemplate.delete(key);
	}

	public Boolean delete(RedisKey key) {
		Assert.notNull(key);
		return delete(key.getKey());
	}

	/**
	 * 模糊删除
	 * @param pre
	 */
	public Long deleteByPrex(String pre) {
		Set<String> keys = redisTemplate.keys(pre);
		if (!CollectionUtils.isEmpty(keys)) {
			return redisTemplate.delete(keys);
		}
		return null;
	}

	/**
	 * 队列获取数量
	 *
	 * @param redisKey
	 * @return
	 */
	public Long listSize(RedisKey redisKey) {
		return redisTemplate.opsForList().size(redisKey.getKey());
	}


	/**
	 *  获取1-1000的随机数  用于加在超时时间后面，防止缓存在同一时间失效
	 *
	 * @param second    超时的秒数
	 * @return
	 */
	private long getTimeout(long second) {
		return second + RANDOM.nextInt(1000) + 1;
	}
}
