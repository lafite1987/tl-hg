package cn.lfy.common;

import cn.lfy.common.exception.ErrorCode;

public class Result<T> {

	private int code = 200;
	private String message = "";
	private T data;
	private String redirect = "";
	
	public static <T> Result<T> success() {
		return new Result<T>();
	}
	
	public static <T> Result<T> fail(int code) {
		Result<T> result = new Result<T>();
		result.setCode(code);
		return result;
	}
	
	public static <T> Result<T> fail() {
		Result<T> result = new Result<T>();
		result.setCode(ErrorCode.SERVER_ERROR.getCode());
		return result;
	}
	
	private Result() {
	}
	
	private Result(int code) {
		this.code = code;
	}
	public int getCode() {
		return code;
	}

	public Result<T> setCode(int code) {
		this.code = code;
		return this;
	}

	public String getMessage() {
		return message;
	}

	public Result<T> setMessage(String message) {
		this.message = message;
		return this;
	}

	public T getData() {
		return data;
	}

	public Result<T> setData(T data) {
		this.data = data;
		return this;
	}

	public String getRedirect() {
		return redirect;
	}

	public Result<T> setRedirect(String redirect) {
		this.redirect = redirect;
		return this;
	}
	
}
