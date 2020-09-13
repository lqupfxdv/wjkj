package com.example.shipment.managment.service;

/**
 * 返回状态
 * @author lqupf
 *
 */
public enum ResultStatusEnum {
	SUCCESS(200, "success"), 
	NOT_FOUND(404, "not found"),
	PARAM_ERROR(400, "param error"),
	UNKNOWN_ERROR(500, "unknown error");

	private Integer code;

	private String msg;

	ResultStatusEnum(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}
