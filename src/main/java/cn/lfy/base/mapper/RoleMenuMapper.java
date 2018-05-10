package cn.lfy.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lfy.base.model.Menu;
import cn.lfy.base.model.RoleMenu;
import cn.lfy.common.mybatis.SuperMapper;

public interface RoleMenuMapper extends SuperMapper<RoleMenu> {
    /**
     * 根据角色查询默认的菜单列表
     * @param operatorId
     * @return
     */
    List<Menu> selectMenuListByRoleId(Long roleId);
    
    /**
     * 根据角色查询默认的菜单列表
     * @param operatorId
     * @return
     */
    List<Menu> selectMenuListByRoleIds(@Param("list")List<Long> list);
    
    /**
     * 删除角色的菜单 
     * @param operatorId
     */
    void deleteByRoleId(Long roleId);
    
    /**
     * 删除某菜单 
     * @param operatorId
     */
    void deleteByMenuId(Long menuId);
    /**
     * 删除
     * @param roleId
     * @param menuId
     */
    void delete(@Param("roleId")Long roleId, @Param("menuId")Long menuId);
}