package cn.lfy.base.service;

import com.baomidou.mybatisplus.service.IService;

import cn.lfy.base.model.User;

public interface UserService extends IService<User> {
	
    /**
     * 通过用户名查询用户
     * @param username
     * @return
     */
    User findByUsername(String username);
    
}