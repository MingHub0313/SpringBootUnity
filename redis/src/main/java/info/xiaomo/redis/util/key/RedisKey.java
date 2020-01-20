package info.xiaomo.redis.util.key;

import java.util.HashMap;
import java.util.Map;

/**
 * @Name RedisKey
 * @Author 900045
 * @Created by 2020/1/20 0020
 */
public interface RedisKey {

	final static String SEPARATE    = "";

	/**
	 * 因为枚举是单例所以只能用这个进行缓存
	 */
	ThreadLocal<Map<RedisKey, String>> SUFFIX_MAP = new ThreadLocal<>();

	/**
	 * 设置后缀
	 *     注意不能使用   setSuffex(s).setSuffex(s)  多次设置的方式,  每次设置都会覆盖之前的配置
	 *
	 * @param suffex
	 * @return
	 */
	default RedisKey setSuffex(String suffex, String... suffexs) {

		Map<RedisKey, String> map = SUFFIX_MAP.get();
		if (map == null) {
			map = new HashMap<RedisKey, String>();
		}
		if (suffexs != null && suffexs.length > 0) {
			StringBuilder sb = new StringBuilder(suffex);
			for (String string : suffexs) {
				sb.append(SEPARATE).append(string);
			}
			map.put(this, sb.toString());
		} else {
			map.put(this, suffex);
		}
		SUFFIX_MAP.set(map);
		return this;
	}

	/**
	 *   后缀
	 *            注意不能使用   setSuffex(s).setSuffex(s)  多次设置的方式,  每次设置都会覆盖之前的配置
	 * @param suffex
	 * @param suffexs
	 * @return
	 */
	default RedisKey setSuffex(Number suffex, String... suffexs) {
		setSuffex(String.valueOf(suffex), suffexs);
		return this;
	}

	/**
	 * 设置后缀
	 *          注意不能使用   setSuffex(s).setSuffex(s)  多次设置的方式,  每次设置都会覆盖之前的配置
	 * @param suffex
	 * @return
	 */
	default RedisKey setSuffex(String suffex) {
		Map<RedisKey, String> map = SUFFIX_MAP.get();
		if (map == null) {
			map = new HashMap<RedisKey, String>();
		}
		map.put(this, suffex);
		SUFFIX_MAP.set(map);
		return this;
	}

	/**
	 * 设置后缀
	 *      注意不能使用   setSuffex(s).setSuffex(s)  多次设置的方式,  每次设置都会覆盖之前的配置
	 * @param suffex
	 * @return
	 */
	default RedisKey setSuffex(Number suffex) {
		setSuffex(String.valueOf(suffex));
		return this;
	}

	default String getSuffix() {
		Map<RedisKey, String> map = SUFFIX_MAP.get();
		return map == null ? null : map.get(this);
	}
	/**
	 * 获取实际 redis key值
	 *
	 * @return
	 */
	String getKey();
}
