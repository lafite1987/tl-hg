package cn.lfy.base.ctrl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cn.lfy.base.dto.UserRoleDTO;
import cn.lfy.base.model.LoginUser;
import cn.lfy.base.model.Role;
import cn.lfy.base.model.User;
import cn.lfy.base.service.RoleService;
import cn.lfy.base.service.UserRoleService;
import cn.lfy.base.service.UserService;
import cn.lfy.common.PageWrapper;
import cn.lfy.common.RestRequest;
import cn.lfy.common.Result;
import cn.lfy.common.exception.ErrorCode;
import cn.lfy.common.exception.GlobalException;
import cn.lfy.common.util.MessageDigestUtil;
import cn.lfy.common.util.UUIDUtil;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/manager/user")
public class UserController extends BaseController {

	@Autowired
	private UserService userService;

	@Autowired
	private RoleService roleService;

	@Autowired
	private UserRoleService userRoleService;

	@RequestMapping(value = "/list.json")
	@ApiOperation(value = "用户列表", httpMethod = "GET", notes = "用户列表接口")
	public @ResponseBody Result<PageWrapper<User>> list(@RequestBody RestRequest<User> restRequest) {
		Result<PageWrapper<User>> result = Result.success();
		Page<User> page = userService.selectPage(restRequest.toPage());
		PageWrapper<User> pageWrapper = PageWrapper.buildPageWrapper(page);
		result.setData(pageWrapper);
		return result;
	}

	@RequestMapping(value = "/{id}/del.json", method = RequestMethod.POST)
	@ApiOperation(value = "删除用户", httpMethod = "POST", notes = "删除指定用户")
	public @ResponseBody Result<Boolean> del(@PathVariable(name = "id") Long id) {
		userService.deleteById(id);
		Result<Boolean> result = Result.success();
		return result;
	}

	@RequestMapping(value = "/{id}/detail.json", method = RequestMethod.GET)
	@ApiOperation(value = "获取用户详情", httpMethod = "GET", notes = "获取用户详情")
	public @ResponseBody Result<User> detail(@PathVariable(name = "id") Long id) {
		Result<User> result = Result.success();
		User user = userService.selectById(id);
		user.setPassword(null);
		result.setData(user);
		return result;
	}

	@RequestMapping(value = "/add.json")
	@ApiOperation(value = "新增用户", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "新增用户")
	public @ResponseBody Result<Void> add(@RequestBody User user) {
		Result<Void> result = Result.success();
		String username = user.getUsername();
		User extuser = userService.findByUsername(username);
		if (extuser != null) {
			throw GlobalException.newGlobalException(ErrorCode.EXISTED, "用户名");
		}
		String salt = UUIDUtil.salt();
		String password = user.getPassword();
		password = MessageDigestUtil.getSHA256(password + salt).toUpperCase();
		user.setSalt(salt);
		user.setPassword(password);
		userService.insert(user);
		return result;
	}

	/**
	 * 更新
	 * 
	 * @throws ApplicationException
	 */
	@RequestMapping(value = "/{id}/update.json")
	@ResponseBody
	@ApiOperation(value = "更新用户", httpMethod = "POST", notes = "更新用户")
	public Result<Void> update(@RequestBody User user) {
		Result<Void> result = Result.success();
		if (StringUtils.isNotBlank(user.getPassword())) {
			try {
				String salt = UUIDUtil.salt();
				String password = MessageDigestUtil
						.getSHA256(user.getPassword() + salt);
				user.setPassword(password);
				user.setSalt(salt);
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			user.setPassword(null);
			user.setSalt(null);
		}
		userService.updateById(user);
		return result;
	}

	@RequestMapping(value = "/{id}/roles.json")
	@ResponseBody
	@ApiOperation(value = "用户角色树", httpMethod = "POST", notes = "用户角色树")
	public Result<UserRoleDTO> roles(@PathVariable(name = "id") Long userId) {
		List<Role> roleTree = new ArrayList<Role>();
		List<Role> list = userRoleService.getRoleListByUserId(userId);
		Set<Long> roleSet = Sets.newHashSet();
		for (Role role : list) {
			roleSet.add(role.getId());
		}
		//获取当前登录用户的角色列表
		List<Role> currentUserRoleList = userRoleService.getRoleListByUserId(1L);
		Set<Role> roles = Sets.newTreeSet();
		roles.addAll(currentUserRoleList);
		
		Set<Long> currentUserRoleIds = Sets.newTreeSet();
		currentUserRoleIds.add(1L);
		
		roleTree.addAll(roles);
		while (!roles.isEmpty()) {
			Set<Role> next = new TreeSet<Role>();
			for (Role role : roles) {
				Role parent = new Role();
				parent.setParentId(role.getId());
				EntityWrapper<Role> entityWrapper = new EntityWrapper<>(parent);
				List<Role> tmpRoles = roleService.selectList(entityWrapper);
				roleTree.addAll(tmpRoles);
				next.addAll(tmpRoles);
			}
			roles = next;
		}
		boolean chkDisabled = false;
		if (currentUserRoleIds.contains(Long.valueOf(1L))
				&& roleSet.contains(Long.valueOf(1L))) {
			chkDisabled = true;
		} else if (roleSet.contains(Long.valueOf(1L))) {
			chkDisabled = true;
		}
		
		Map<Long, Role> map = Maps.newHashMap();
		for(Role role : roleTree) {
			boolean checked = roleSet.contains(role.getId());
			role.setChecked(checked);
			role.setDisabled(chkDisabled);
			map.put(role.getId(), role);
		}
		Set<Role> trees = Sets.newTreeSet();
		for(Role role : roleTree) {
			Long parentId = role.getParentId();
			Role parent = map.get(parentId);
			if(parent != null) {
				parent.getChildren().add(role);
			} else {
				trees.add(role);
			}
		}
		Result<UserRoleDTO> result = Result.success();
		UserRoleDTO userRoleDTO = new UserRoleDTO();
		userRoleDTO.setList(Lists.newArrayList(trees));
		userRoleDTO.setChecked(Lists.newArrayList(roleSet));
		result.setData(userRoleDTO);
		return result;
	}

	@RequestMapping(value = "/{id}/saveRoles.json")
	@ResponseBody
	@ApiOperation(value = "保存用户角色", httpMethod = "POST", notes = "保存用户角色")
	public Object save(
			@PathVariable(name = "id") Long userId,
			@RequestParam(name = "roleIds", required = true) String roleIdsString,
			LoginUser currentUser) {
		Result<Void> result = Result.success();
		List<Long> roleIds = Lists.newArrayList();
		if (StringUtils.isNotBlank(roleIdsString)) {
			Iterator<String> it = Splitter.on(",").split(roleIdsString)
					.iterator();
			while (it.hasNext()) {
				roleIds.add(Long.parseLong(it.next()));
			}
		}
		if (null == userId || userId <= 0) {
			throw GlobalException.newGlobalException(ErrorCode.PARAM_INVALID, "userId");
		}

		User user = userService.selectById(userId);
		if (null == user) {
			throw GlobalException.newGlobalException(ErrorCode.NOT_EXISTED, "User");
		}
		List<Role> roleListByUserId = userRoleService.getRoleListByUserId(1L);
		Set<Role> currentUserRoles = Sets.newTreeSet();
		currentUserRoles.addAll(roleListByUserId);
		userRoleService.saveRoles(user.getId(), roleIds, currentUserRoles);
		return result;
	}
}
