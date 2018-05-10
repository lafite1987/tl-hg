package cn.lfy.base.mapper;

import cn.lfy.base.model.User;
import cn.lfy.common.mybatis.SuperMapper;

public interface UserMapper extends SuperMapper<User> {

    /**
     * 根据登录名查询，username唯一
     * @param username
     * @return
     */
    User selectByUsername(String username);

}