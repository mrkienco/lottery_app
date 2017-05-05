package com.app.lottery.bean;

public enum RequestStatus {

	FAIL(0, "Fail"),
	SUCCES(1, "Success"),
	WRONG_SIGNATURE(2, "Signature is wrong"),
	PARAMS_INVALID(3, "Params invalid");
	
	private String message;
	private int code;
	
	private RequestStatus(int code, String message) {
		this.code = code;
		this.message = message;
	}
	
	public String getMessage() {
		return this.message;
	}
	
	public int getCode() {
		return this.code;
	}
}
