package cn.lfy.common.exception;

public enum ErrorCode {

	BAD_REQUEST(400, "请求参数有误"),
	NOT_FOUND(404, "请求资源未找到"),
	PARAM_INVALID(450, "参数无效"),
	
	SERVER_ERROR(500, "服务器异常，请稍后再试"),
	TOKEN_INVALID(501, "访问令牌无效"),
	REGISTER_CODE_ERROR(502, "您输入的激活码错误，请重新输入"),
	NO_LOGIN(401, "未登录，请先登录"),
	UNAUTHORIZED(403, "未授权"),
	UPLOAD_FILE_EMPTY(420, "上传文件为空"),
	UPLOAD_FILE_FAILED(421, "上传文件失败"),
	
	USERNAME_PASSWORD_ERROR(600, "用户名或密码错误"),
	EXISTED(601, "{0}已存在"),
	NOT_EXISTED(602, "{0}不存在"),
	;
	private int code;
	private String message;
	
	private ErrorCode(int code, String message) {
		this.code = code;
		this.message = message;
	}
	public int getCode() {
		return this.code;
	}
	public String getMessage() {
		return this.message;
	}
}
