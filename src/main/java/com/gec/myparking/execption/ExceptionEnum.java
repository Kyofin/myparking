package com.gec.myparking.execption;

public enum ExceptionEnum {
	AGE_EXCEPTION(100,"女生年龄信息异常"),
	HEIGHT_EXCEPTION(101,"女生身高信息异常");

	private Integer code;

	private String msg;

	ExceptionEnum(Integer code, String msg) {
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
