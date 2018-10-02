package cn.lfy.base.interceptor;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Sets;
import com.hg.tl_hg.service.ConfigService;

import cn.lfy.common.Result;
import cn.lfy.common.exception.ErrorCode;
import cn.lfy.common.util.JwtToken;

@Component
public class ApiInterceptor extends HandlerInterceptorAdapter {

	private static final Set<String> EXCLUSIVE = Sets.newConcurrentHashSet();
	
	@Autowired
	private ConfigService configService;
	
	static {
		EXCLUSIVE.add("/api/registrationCode/login");
		EXCLUSIVE.add("/api/checkUpdate");
		EXCLUSIVE.add("/api/advertisement/recommend");
		EXCLUSIVE.add("/api/classify/list");
		EXCLUSIVE.add("/api/concatUs/info");
	}
	private String getAuthorization(HttpServletRequest request) {
		String token = request.getParameter("token");
		if(StringUtils.isBlank(token)) {
			token = request.getHeader("Authorization");
		}
		return token;
	}
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		String uri = request.getRequestURI();
		if(!configService.isNeedRegistration() || EXCLUSIVE.contains(uri) || uri.startsWith("/api/movie/index/")) {
			return super.preHandle(request, response, handler);
		}
		String token = getAuthorization(request);
		Long accountId = JwtToken.verify(token);
		if(accountId == null || accountId == 0L) {
			response.setContentType("application/json;charset=UTF-8");
			Result<String> result = Result.<String>fail(ErrorCode.TOKEN_INVALID.getCode()).setMessage(ErrorCode.TOKEN_INVALID.getMessage()).setData("");
			String json = JSON.toJSONString(result);
			response.getWriter().write(json);
			response.getWriter().flush();
			return false;
		}
		return super.preHandle(request, response, handler);
	}

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

}
