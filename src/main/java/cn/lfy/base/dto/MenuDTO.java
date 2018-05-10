package cn.lfy.base.dto;

import lombok.Data;

@Data
public class MenuDTO {

	private Long id;
	private String permUrl;
	private String permName;
	private String iconClass;
	private Long parentPermId;
}
