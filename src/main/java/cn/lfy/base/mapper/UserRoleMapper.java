package cn.lfy.base.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lfy.base.model.Role;
import cn.lfy.base.model.UserRole;
import cn.lfy.common.mybatis.SuperMapper;

public interface UserRoleMapper extends SuperMapper<UserRole> {

    /**
     * 根据内勤人员id获取菜单列表
     * @param userId
     * @return
     */
    List<Role> getRoleListByUserId(Long userId);
    
    /**
     * 删除内勤人员ID的菜单 
     * @param userId
     */
    void deleteByUserId(Long  userId);
    
    /**
     * 删除某菜单 
     * @param menuId
     */
    void deleteByRoleId(Long  roleId);
    
    /**
     * 删除某菜单 
     * @param userId
     * @param roleId
     */
    void delete(@Param("userId")Long userId, @Param("roleId")Long roleId);
    
}