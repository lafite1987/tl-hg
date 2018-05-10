package cn.lfy.base.mapper;

import java.util.Collection;
import java.util.List;

import org.apache.ibatis.annotations.Param;

import cn.lfy.base.model.Role;
import cn.lfy.common.mybatis.SuperMapper;

public interface RoleMapper extends SuperMapper<Role> {

    /**
     * 根據角色ID列表查詢角色列表
     * @param list
     */
    List<Role> getRoles(@Param("list")Collection<Long> list);
    
}