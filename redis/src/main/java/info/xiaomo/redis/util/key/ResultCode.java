package info.xiaomo.redis.util.key;

import java.util.HashMap;
import java.util.Map;

/**
 * 返回状态码枚举
 * @Name ResultCode
 * @Author 900045
 * 各模块返回码可实现 RespCode接口单独定义
 * @Created by 2020/1/20 0020
 */
public enum ResultCode implements RespCode{

	SUCCESS(1000, "成功"),
	FIELD(9000, "操作失败!"),


	FAIL(9999, "系统异常，请刷新后重试", "system error ! "),
	METHOD_CALL_PARAMETER_ERROR(100012, "请求参数有误，请按照规则填写！"),
	;
	/**
	 * ===================成员变量
	 */

	/**
	 * 编码
	 */
	private int code;
	/**
	 * 中文描述
	 */
	private String desc;
	/**
	 * 英文描述
	 */
	private String enDesc;
	/**
	 * 返回码MAP
	 */
	private static Map<Integer, RespCode> map;

	static {
		// 初始化
		map = new HashMap<>();
		for (ResultCode code : ResultCode.values()) {
			map.put(code.getCode(), code);
		}
	}

	private ResultCode(int code, String desc) {
		this.code = code;
		this.desc = desc;
	}

	private ResultCode(int code, String desc, String enDesc) {
		this.code = code;
		this.desc = desc;
		this.enDesc = enDesc;
	}

	public static RespCode getType(int code) {
		if (code <= 0) {
			return null;
		}
		return map.get(code);
	}

	@Override
	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	@Override
	public String getDesc() {
		return desc;
	}

	@Override
	public void setDesc(String desc) {
		this.desc = desc;
	}

	/**
	 * 获取返回码反描述
	 *
	 * @param
	 * @return
	 */
	public static String getDesc(int code) {
		RespCode respCode = map.get(code);
		return respCode == null ? "" : respCode.getDesc();
	}

	@Override
	public String getEnDesc() {
		return enDesc;
	}

	public void setEnDesc(String enDesc) {
		this.enDesc = enDesc;
	}

	@Override
	public boolean print() {
		return false;
	}
}
