package cn.lfy.base.service.impl;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import cn.lfy.base.mapper.RoleMenuMapper;
import cn.lfy.base.model.Menu;
import cn.lfy.base.model.Role;
import cn.lfy.base.model.RoleMenu;
import cn.lfy.base.service.RoleMenuService;
import cn.lfy.base.service.RoleService;
import cn.lfy.common.exception.ErrorCode;
import cn.lfy.common.exception.GlobalException;

@Service
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

    @Autowired
    private RoleMenuMapper roleMenuDAO;
    @Autowired
    private RoleService roleService;
    
    @Override
    public List<Menu> getMenuListByRoleId(Long roleId) {
        return roleMenuDAO.selectMenuListByRoleId(roleId);
    }

    @Override
    public void deleteByRoleId(Long roleId) {
        roleMenuDAO.deleteByRoleId(roleId);
    }

    @Override
    public void deleteByMenuId(Long menuId) {
        roleMenuDAO.deleteByMenuId(menuId);
    }

    public void saveMenus(Long roleId, Set<Long> nowMenu, List<Long> currentUserRoleIds) {
    	if(roleId == 1) {
    		throw GlobalException.newGlobalException(ErrorCode.UNAUTHORIZED);
    	}
    	Role role = roleService.selectById(roleId);
    	List<Menu> parentRoleMenus = getMenuListByRoleId(role.getParentId());
    	List<Menu> roleMenus = getMenuListByRoleId(roleId);
		Set<Long> roleSet = Sets.newHashSet();
		for(Menu m : roleMenus) {
			roleSet.add(m.getId());
		}
		Set<Long> delSet = Sets.difference(roleSet, nowMenu);
		Set<Long> newSet = Sets.difference(nowMenu, roleSet);
		List<Menu> currentUserMenus = selectMenuListByRoleIds(currentUserRoleIds);
		Set<Long> currentUserMenuSet = Sets.newHashSet();
		Set<Long> parentRoleMenuSet = Sets.newHashSet();
		for(Menu menu : parentRoleMenus) {
			parentRoleMenuSet.add(menu.getId());
		}
		if(!parentRoleMenuSet.containsAll(newSet)) {
    		throw GlobalException.newGlobalException(ErrorCode.UNAUTHORIZED);
    	}
		for(Menu menu : currentUserMenus) {
			currentUserMenuSet.add(menu.getId());
		}
		if(!currentUserMenuSet.containsAll(newSet)) {
    		throw GlobalException.newGlobalException(ErrorCode.UNAUTHORIZED);
    	}
		for(Long menuId : delSet) {
			roleMenuDAO.delete(roleId, menuId);
		}
		
		for(Long menuId : newSet) {
			RoleMenu record = new RoleMenu();
			record.setRoleId(roleId);
			record.setMenuId(menuId);
			roleMenuDAO.insert(record);
		}
    }

	@Override
	public List<Menu> selectMenuListByRoleIds(List<Long> list) {
		if(list == null || list.isEmpty()) {
			List<Long> tmp = Lists.newArrayList(0L);
			return roleMenuDAO.selectMenuListByRoleIds(tmp);
		}
		return roleMenuDAO.selectMenuListByRoleIds(list);
	}

}
