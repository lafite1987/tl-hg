package com.hg.tl_hg.common;

public class Result<T> {

	private int code = 200;
	private String message = "";
	private T data;
	private String redirect = "";
	
	public static <T> Result<T> success() {
		return new Result<T>();
	}
	
	public static <T> Result<T> fail(int code) {
		return new Result<T>();
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
