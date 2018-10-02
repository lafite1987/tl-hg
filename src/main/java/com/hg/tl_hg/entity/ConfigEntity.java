package com.hg.tl_hg.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@TableName("t_config")
@Data
public class ConfigEntity {

	@TableId(value="id",type=IdType.AUTO)
    private Long id;
	
	private String group;
	
	private String key;
	
	private String value;
	
	private String description;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
}
