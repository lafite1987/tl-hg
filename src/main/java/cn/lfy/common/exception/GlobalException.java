package cn.lfy.common.exception;

public class GlobalException extends RuntimeException {
	
	private static final long serialVersionUID = 6043960717295306098L;
	
	private ErrorCode errorCode;

	private Object[] messageParams;
	
    private GlobalException() {
        super();
    }
    
    private GlobalException(String message, Throwable cause) {
        super(message, cause);
    }
    
    public static GlobalException newGlobalException(ErrorCode errorCode, Object ...messageParams) {
    	return newGlobalException(errorCode, null, messageParams);
    }
    
    public static GlobalException newGlobalException(ErrorCode errorCode, Throwable cause, Object ...messageParams) {
    	GlobalException globalException = new GlobalException(errorCode.name(), cause);
    	globalException.setErrorCode(errorCode);
    	globalException.setMessageParams(messageParams);
    	return globalException;
    }

	public ErrorCode getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(ErrorCode errorCode) {
		this.errorCode = errorCode;
	}

	public Object[] getMessageParams() {
		return messageParams;
	}

	public void setMessageParams(Object[] messageParams) {
		this.messageParams = messageParams;
	}
	
}
