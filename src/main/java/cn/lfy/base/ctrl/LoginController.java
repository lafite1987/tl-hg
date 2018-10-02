package cn.lfy.base.ctrl;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cn.lfy.base.dto.MenuDTO;
import cn.lfy.base.form.LoginForm;
import cn.lfy.base.model.LoginUser;
import cn.lfy.base.model.Menu;
import cn.lfy.base.model.Role;
import cn.lfy.base.model.User;
import cn.lfy.base.service.RoleMenuService;
import cn.lfy.base.service.UserRoleService;
import cn.lfy.base.service.UserService;
import cn.lfy.common.Constants;
import cn.lfy.common.Result;
import cn.lfy.common.exception.ErrorCode;
import cn.lfy.common.exception.GlobalException;
import cn.lfy.common.util.JwtToken;
import cn.lfy.common.util.MessageDigestUtil;
import cn.lfy.common.util.Strings;
import io.swagger.annotations.ApiOperation;

@Controller
public class LoginController extends BaseController {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRoleService userRoleService;
    
    @Autowired
	private RoleMenuService roleMenuService;
    
    @RequestMapping(value = "/manager/menu", method = RequestMethod.GET)
    @ResponseBody
    @ApiOperation(value = "用户菜单", httpMethod = "GET", notes = "用户菜单")
    public Result<List<MenuDTO>> menu(LoginUser currentUser) throws GlobalException {
    	Result<List<MenuDTO>> result = Result.success();
    	List<Menu> menus = roleMenuService.selectMenuListByRoleIds(Lists.newArrayList(1L));
        List<MenuDTO> menuList = Lists.newArrayList();
        for(Menu menu : menus) {
        	if(menu.getOnMenu() == 1 && menu.getId() != 1) {
        		MenuDTO menuDTO = new MenuDTO();
        		menuDTO.setId(menu.getId());
        		Long parentId = 0L;
        		if(menu.getParentId() != 1L) {
        			parentId = menu.getParentId();
        		}
        		menuDTO.setParentPermId(parentId);
        		menuDTO.setPermName(menu.getName());
        		menuDTO.setPermUrl(menu.getUrl());
        		menuDTO.setIconClass(menu.getIcon());
        		menuList.add(menuDTO);
        	}
        }
        result.setData(menuList);
        return result;
    }
    
    @RequestMapping("/login.json")
    @ResponseBody
    @ApiOperation(value = "登录接口", httpMethod = "POST", notes = "登录接口", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE)
    public Result<String> doLogin(@RequestBody LoginForm loginForm) {
    	String username = loginForm.getUsername(), password = loginForm.getPassword();
        if (null == username || username.trim().length() == 0
                || null == password || password.length() == 0) {
            throw GlobalException.newGlobalException(ErrorCode.PARAM_INVALID, "用户名或密码");
        }
        
        LoginUser account = getLoginUser(username);
        User user = account.getUser();
        if (user == null) {
            throw GlobalException.newGlobalException(ErrorCode.SERVER_ERROR);
        }
        
        password = MessageDigestUtil.getSHA256(password + user.getSalt());
        
        if (!Strings.slowEquals(password, user.getPassword())) {
        	throw GlobalException.newGlobalException(ErrorCode.USERNAME_PASSWORD_ERROR, "用户名或密码");
        }
        account.setId(user.getId());
        List<Menu> menus = roleMenuService.selectMenuListByRoleIds(Lists.newArrayList(account.getRoleIds()));
        Set<String> uriSet = Sets.newTreeSet();
        for(Menu menu : menus) {
        	uriSet.add(menu.getUrl());
        }
        account.setUriSet(uriSet);
//        HttpServletRequest request = getRequest();
        String token = JwtToken.createToken(account.getId());
//        request.getSession().setAttribute(Constants.SESSION_LOGIN_USER, account);
        Result<String> result = Result.success();
        result.setData(token);
        return result;
    }
    
    private LoginUser getLoginUser(String username){
        User dbUser = userService.findByUsername(username);
        if(dbUser == null) {
        	throw GlobalException.newGlobalException(ErrorCode.USERNAME_PASSWORD_ERROR, "用户名或密码错误");
        }
        List<Role> roleList = userRoleService.getRoleListByUserId(dbUser.getId());
        LoginUser account = new LoginUser();
        Set<Role> roleSet = new TreeSet<Role>();
        roleSet.addAll(roleList);
        account.setRoles(roleSet);
        account.setUser(dbUser);

        return account;
    }

    @RequestMapping(value = "/manager/getUserInfo.json")
	@ResponseBody
	public Result<Map<String, Object>> getUserInfo(@RequestHeader("Authorization") String authorization) {
    	String token = authorization.substring(6);
    	Long userId = JwtToken.verify(token);
    	User user = userService.selectById(userId);
    	Result<Map<String, Object>> result = Result.success();
		Map<String, Object> userInfo = Maps.newHashMap();
		userInfo.put("id", userId);
		userInfo.put("name", user.getNickname());
		result.setData(userInfo);
		return result;
	}
    
    @RequestMapping("/manager/logout")
    @ResponseBody
    @ApiOperation(value = "登出接口", httpMethod = "GET", notes = "登出接口")
    public Result<Void> logout() {
    	HttpServletRequest request = getRequest();
    	Result<Void> result = Result.success();
        HttpSession session = request.getSession();
        session.removeAttribute(Constants.SESSION_LOGIN_USER);
        session.invalidate();
        result.setRedirect("/console/login.html");
        return result;
    }
    

}
