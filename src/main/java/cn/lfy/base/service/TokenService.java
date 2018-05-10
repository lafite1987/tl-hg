package cn.lfy.base.service;

import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cn.lfy.base.model.LoginUser;
import cn.lfy.base.model.Menu;
import cn.lfy.base.model.Role;
import cn.lfy.base.model.User;
import cn.lfy.common.exception.ErrorCode;
import cn.lfy.common.exception.GlobalException;
import cn.lfy.common.util.JwtToken;

@Component
public class TokenService {

	@Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;
    
    @Autowired
	private RoleMenuService roleMenuService;
    
	private LoadingCache<Long, LoginUser> cache = CacheBuilder.newBuilder()
			.expireAfterWrite(60, TimeUnit.MINUTES).maximumSize(1000)
			.build(new CacheLoader<Long, LoginUser>() {

				@Override
				public LoginUser load(Long userId) throws Exception {
					return getLoginUserByUserId(userId);
				}
	});
	
	public void refresh(LoginUser loginUser) {
		cache.put(loginUser.getId(), loginUser);
	}
	
	public LoginUser getLoginUserByUserId(Long userId) {
		User user = userService.selectById(userId);
		if(user == null) {
			throw GlobalException.newGlobalException(ErrorCode.NOT_EXISTED, "user[id=" + userId + "]");
		}
		List<Role> roleList = userRoleService.getRoleListByUserId(userId);
        LoginUser loginUser = new LoginUser();
        Set<Role> roleSet = new TreeSet<Role>();
        roleSet.addAll(roleList);
        loginUser.setRoles(roleSet);
        loginUser.setUser(user);
        
        List<Menu> menus = roleMenuService.selectMenuListByRoleIds(Lists.newArrayList(loginUser.getRoleIds()));
        Set<String> uriSet = Sets.newTreeSet();
        for(Menu menu : menus) {
        	uriSet.add(menu.getUrl());
        }
        loginUser.setUriSet(uriSet);
        return loginUser;
	}
	
	public LoginUser getLoginUser() {
		HttpServletRequest request = ((ServletRequestAttributes)RequestContextHolder.getRequestAttributes()).getRequest();
		return getLoginUser(request);
	}
	
	public LoginUser getLoginUser(HttpServletRequest request) {
		String token = request.getHeader("Authorization");
		if(StringUtils.isBlank(token)) {
			throw GlobalException.newGlobalException(ErrorCode.NO_LOGIN);
		}
		token = token.substring(7);
		Long userId = JwtToken.verify(token);
		try {
			LoginUser loginUser = cache.get(userId);
			return loginUser;
		} catch (ExecutionException e) {
			throw GlobalException.newGlobalException(ErrorCode.TOKEN_INVALID);
		}
	}
}
