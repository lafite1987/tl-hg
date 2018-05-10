package cn.lfy.base.dto;

import java.util.List;

import cn.lfy.base.model.Menu;
import lombok.Data;

@Data
public class RoleMenuDTO {

	private List<Menu> list;
	
	private List<Long> checked;
}
