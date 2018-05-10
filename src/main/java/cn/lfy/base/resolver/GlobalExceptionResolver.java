package cn.lfy.base.resolver;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import cn.lfy.common.exception.ErrorCode;
import cn.lfy.common.exception.GlobalException;
import cn.lfy.common.util.ParamUtils;

@Component
public class GlobalExceptionResolver implements HandlerExceptionResolver, Ordered {

	protected static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionResolver.class);
	
	@Autowired
	private MessageSource messageSource;
	
	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception e) {
		ModelAndView empty = new ModelAndView();
		Map<String, Object> paramMap = ParamUtils.getParameter(request);
		
		String params = ParamUtils.getParameterText(paramMap, null, "&");
		String requestUrl = request.getRequestURI() + (null == params || params.isEmpty() ? "" : "?" + params);

		ErrorCode errorCode = ErrorCode.SERVER_ERROR;
		String errorMessage = errorCode.getMessage();
		if (e instanceof GlobalException) {
			GlobalException ae = (GlobalException) e;
			errorCode = ae.getErrorCode();
			try {
				errorMessage = messageSource.getMessage(errorCode.name(), ae.getMessageParams(), request.getLocale());
			} catch (Throwable e2) {
				LOGGER.error("ErrorCode[code=" + errorCode.getCode() + " ,message=" + ae.getMessage() + "]", e2);
				errorMessage = ae.getMessage();
			}
		} else if(e instanceof MethodArgumentNotValidException) {
			errorCode = ErrorCode.BAD_REQUEST;
			MethodArgumentNotValidException ae = (MethodArgumentNotValidException) e;
			List<ObjectError> allErrors = ae.getBindingResult().getAllErrors();
			StringBuilder errorMsg = new StringBuilder();
			for(ObjectError error : allErrors) {
				errorMsg.append(error.getDefaultMessage()).append(";");
			}
			errorMessage = errorMsg.toString();
		} else if(e instanceof NoHandlerFoundException) {
			errorCode = ErrorCode.NOT_FOUND;
			errorMessage = e.getMessage();
		}
		
		String eMessage = String.format("ErrorCode[code=%d, message=%s,] RequestBody=%s", errorCode.getCode(), errorMessage,requestUrl);
		LOGGER.error(eMessage, e);
		
		try {
			response.setContentType("application/json;charset=UTF-8");
			response.getWriter().write("{\"code\":" + errorCode.getCode() + ",\"message\":\"" + errorMessage + "\"}");
			response.getWriter().flush();
			response.getWriter().close();
		} catch (IOException e1) {
			LOGGER.error("Response Boby Exception", e1);
		}
		return empty;
	}
}
