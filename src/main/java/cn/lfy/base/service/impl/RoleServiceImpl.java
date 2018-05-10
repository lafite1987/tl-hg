package cn.lfy.base.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;

import cn.lfy.base.mapper.RoleMapper;
import cn.lfy.base.model.Role;
import cn.lfy.base.service.RoleService;

@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private RoleMapper roleDAO;
    
    @Override
    public boolean insert(Role record) {
    	Role parent = getByIdInCache(record.getParentId());
        record.setLevel(parent.getLevel() + 1);
    	record.setParentIdPath(parent.getParentIdPath() + record.getParentId() + "$");
        return roleDAO.insert(record) > 0;
    }

    @Cacheable(value = "commonCache", key = "'Role_id_' + #id")
    @Override
    public Role getByIdInCache(Long id) {
        return this.selectById(id);
    }

}
