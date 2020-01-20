package info.xiaomo.redis.util.exception;

import info.xiaomo.redis.util.key.RespCode;

/**
 * @Name BaseExcepion
 * @Author 900045
 * @Created by 2020/1/20 0020
 */
public interface BaseExcepion {

	/**
	 * 获取返回码
	 *
	 * @return
	 */
	RespCode getCode();
}
