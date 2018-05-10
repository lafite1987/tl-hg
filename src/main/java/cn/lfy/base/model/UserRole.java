package cn.lfy.base.model;

import com.baomidou.mybatisplus.annotations.TableName;

import cn.lfy.common.mybatis.SuperEntity;

@TableName("user_role")
public class UserRole extends SuperEntity {
	
    private static final long serialVersionUID = 1L;

    private Long userId;

    private Long roleId;

	public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

	public Long getRoleId() {
		return roleId;
	}

	public void setRoleId(Long roleId) {
		this.roleId = roleId;
	}

}
