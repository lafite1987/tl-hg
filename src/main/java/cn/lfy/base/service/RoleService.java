package cn.lfy.base.service;

import com.baomidou.mybatisplus.service.IService;

import cn.lfy.base.model.Role;

public interface RoleService extends IService<Role> {

    /**
     * 根据主键查询记录
     */
    Role getByIdInCache(Long id);
}