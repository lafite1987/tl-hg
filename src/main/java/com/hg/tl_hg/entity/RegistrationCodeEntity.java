package com.hg.tl_hg.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 注册码
 * @author honeyleo
 *
 */
@TableName("t_registration_code")
@Data
public class RegistrationCodeEntity {

	@TableId(value="id",type=IdType.AUTO)
    private Long id;
	/**
	 * 注册码有效天数
	 */
	private Integer days;
	
	private String code;
	/**
	 * 注册码激活时间
	 */
	private Long activateTime;
	/**
	 * 注册码到期时间，从注册码激活时间加上有效天数为到期时间
	 */
	private Long expireTime;
	
	private Integer state;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
}
