package info.xiaomo.redis.util.key;

/**
 * 通用返回码定义
 * @Name RespCode
 * @Author 900045
 * @Created by 2020/1/20 0020
 */
public interface RespCode {

	/**
	 * 获取返回码
	 *
	 * @return
	 */
	int getCode();

	/**
	 *   是否打印详情日志
	 *
	 * @return
	 */
	boolean print();

	/**
	 * 获取返回描述
	 *
	 * @return
	 */
	String getDesc();

	void setDesc(String desc);

	String getEnDesc();
}
