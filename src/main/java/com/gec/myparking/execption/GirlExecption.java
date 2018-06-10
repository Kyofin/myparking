package com.gec.myparking.execption;

public class GirlExecption extends RuntimeException {

	private Integer code;

	public GirlExecption(ExceptionEnum exceptionEnum) {
		super(exceptionEnum.getMsg());
		this.code = exceptionEnum.getCode();

	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}
}
