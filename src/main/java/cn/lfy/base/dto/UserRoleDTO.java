package cn.lfy.base.dto;

import java.util.List;

import cn.lfy.base.model.Role;
import lombok.Data;

@Data
public class UserRoleDTO {

	private List<Role> list;
	
	private List<Long> checked;
}
