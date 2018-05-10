package cn.lfy.base.ctrl;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.apache.commons.lang3.StringUtils;
import org.omg.CORBA.portable.ApplicationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import cn.lfy.base.dto.RoleMenuDTO;
import cn.lfy.base.model.LoginUser;
import cn.lfy.base.model.Menu;
import cn.lfy.base.model.Role;
import cn.lfy.base.service.MenuService;
import cn.lfy.base.service.RoleMenuService;
import cn.lfy.base.service.RoleService;
import cn.lfy.common.Result;
import cn.lfy.common.exception.ErrorCode;
import cn.lfy.common.exception.GlobalException;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/manager/role")
public class RoleController {

    public static final int listPageSize = 20;

    @Autowired
    private RoleService roleService;
    
    @Autowired
    private MenuService menuService;
    
    @Autowired
    private RoleMenuService roleMenuService;

    @RequestMapping(value = "/tree.json", method = RequestMethod.GET)
    @ApiOperation(value = "角色树", httpMethod = "GET", notes = "角色树")
    public @ResponseBody Result<List<Role>> tree() {
    	Set<Role> roleTree = Sets.newTreeSet();
    	List<Role> userRoles = roleService.selectBatchIds(Lists.newArrayList(1L));
//    	List<Role> userRoles = roleService.selectBatchIds(Lists.newArrayList(13L, 14L, 15L));
    	Set<Role> roles = Sets.newTreeSet();
    	roles.addAll(userRoles);
    	roleTree.addAll(roles);
    	while(!roles.isEmpty()) {
    		Set<Role> next = new TreeSet<Role>();
    		for(Role role : roles) {
    			Role parent = new Role();
    			parent.setParentId(role.getId());
    			EntityWrapper<Role> entityWrapper = new EntityWrapper<Role>(parent);
    			List<Role> tmpRoles = roleService.selectList(entityWrapper);
    			roleTree.addAll(tmpRoles);
    			next.addAll(tmpRoles);
    		}
    		roles = next;
    	}
		Map<Long, Role> map = Maps.newHashMap();
		for(Role role : roleTree) {
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
        Result<List<Role>> result = Result.success();
        result.setData(Lists.newArrayList(trees));
    	return result;
    }

    @RequestMapping(value = "/{id}/del.json", method = RequestMethod.POST)
    @ApiOperation(value = "删除角色", httpMethod = "POST", notes = "删除角色")
    public @ResponseBody Result<Void> del(@PathVariable Long id) throws ApplicationException {
    	Result<Void> result = Result.success();;
        roleService.deleteById(id);
        return result;
    }

    @RequestMapping("/{id}/detail.json")
    @ResponseBody
    @ApiOperation(value = "角色信息", httpMethod = "GET", notes = "角色信息")
    public Result<Role> detail(@PathVariable Long id) throws ApplicationException {
    	Result<Role> result = Result.success();;
        Role role = roleService.selectById(id);
        result.setData(role);
        return result;
    }

    @RequestMapping(value = "/add.json", method = RequestMethod.POST)
    @ApiOperation(value = "添加角色", httpMethod = "POST", notes = "添加角色")
    public @ResponseBody Result<Role> add(@RequestBody Role role) throws ApplicationException {
    	Result<Role> result = Result.success();;
    	roleService.insert(role);
    	result.setData(role);
        return result;
    }

    @RequestMapping(value = "/{id}/update.json", method = RequestMethod.POST)
    @ResponseBody
    @ApiOperation(value = "更新角色", httpMethod = "POST", notes = "更新角色")
    public Result<Void> update(@RequestBody Role role) throws ApplicationException {
    	Result<Void> result = Result.success();
    	roleService.updateById(role);
        return result;
    }
    
    /**
	 * 角色权限列表：角色拥有的权限默认选上
	 * @date 2015-10-09
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/{id}/privileges.json", method = RequestMethod.GET)
	@ApiOperation(value = "角色权限树", httpMethod = "GET", notes = "角色权限树")
	public @ResponseBody Result<RoleMenuDTO> privileges(@PathVariable Long id) {
		Set<Long> userRoleIds = Sets.newTreeSet();
		userRoleIds.add(1L);
		Role role = roleService.selectById(id);
		List<Menu> menus = null;
		if(userRoleIds.contains(1L)) {
			menus = menuService.selectList(null);
		} else {
			if(userRoleIds.contains(role.getId())) {
				menus = roleMenuService.getMenuListByRoleId(role.getId());
			} else {
				menus = roleMenuService.getMenuListByRoleId(role.getParentId());
			}
		}
		
		List<Menu> roleMenus = roleMenuService.getMenuListByRoleId(id);
		HashSet<Long> roleMenuIdSet = Sets.newHashSet();
		for(Menu m : roleMenus) {
			roleMenuIdSet.add(m.getId());
		}
		List<Long> checkedList = Lists.newArrayList();
		Map<Long, Menu> map = Maps.newHashMap();
		for(Menu menu : menus) {
			boolean checked = false;
			boolean chkDisabled = false;
			if(userRoleIds.contains(1L) && role.getId() == 1) {
				checked = true;
				chkDisabled = true;
			} else {
				checked = roleMenuIdSet.contains(menu.getId());
			}
			if(checked) {
				checkedList.add(menu.getId());
			}
			menu.setDisabled(chkDisabled);
			menu.setChecked(checked);
			map.put(menu.getId(), menu);
		}
		List<Menu> treeList = Lists.newArrayList();
		for(Menu menu : menus) {
			Menu parent = map.get(menu.getParentId());
			if(parent != null) {
				parent.getChildren().add(menu);
			} else {
				treeList.add(menu);
			}
		}
		Result<RoleMenuDTO> result = Result.success();
		RoleMenuDTO roleMenuDTO = new RoleMenuDTO();
		roleMenuDTO.setList(treeList);
		roleMenuDTO.setChecked(checkedList);
		result.setData(roleMenuDTO);
		return result;
	}
	/**
	 * 保存角色对应权限：做差集
	 * 删除权限：（1，2，3，4）-（1，2，5）=3，4
	 * 新增权限：（1，2，5）-（1，2，3，4）=5
	 * @param request
	 * @return
	 * @throws AdminException
	 */
	@RequestMapping(value = "/privileges/save.json", method = RequestMethod.GET)
	@ApiOperation(value = "角色权限保存", httpMethod = "POST", notes = "角色权限保存")
	public @ResponseBody Result<Void> savePrivileges(Long roleId, String menuIds, LoginUser currentUser) throws ApplicationException
	{
		if (null == roleId || roleId <= 0)
		{
			throw GlobalException.newGlobalException(ErrorCode.PARAM_INVALID, "roleId");
		}
		if (null == menuIds)
        {
			throw GlobalException.newGlobalException(ErrorCode.PARAM_INVALID, "menuIds");
        }
		Role role = roleService.selectById(roleId);
		if (null == role)
		{
			throw GlobalException.newGlobalException(ErrorCode.NOT_EXISTED, "角色");
		}
		Iterator<String> it = Splitter.on(",").trimResults().split(menuIds).iterator();
		Set<Long> nowSet = Sets.newHashSet();
		while(it.hasNext()) {
			String menuId = it.next();
			if(StringUtils.isNotBlank(menuId)) {
				nowSet.add(Long.valueOf(menuId));
			}
			
		}
		List<Long> currentUserRoleIds = Lists.newArrayList(1L);
		roleMenuService.saveMenus(roleId, nowSet, currentUserRoleIds);
		Result<Void> result = Result.success();
		return result;
	}

}
