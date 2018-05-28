package com.hg.tl_hg.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@TableName("t_version")
public class VersionEntity {

	@TableId(value="id",type=IdType.AUTO)
    private Long id;
	
	private String name;
	/**
	 * 根据该值比较获取最新版本
	 */
	private Integer VersionCode;
	/**
	 * 1.0.1
	 */
	private String versionName;
	/**
	 * 更新内容
	 */
	private Integer content;
	/**
	 * 1-IOS；2-Android；
	 */
	@ApiModelProperty(notes = "1-IOS；2-Android")
	private Integer type;
	/**
	 * 封面地址
	 */
	private transient String imageUrl;
	private String imagePath;
	/**
	 * 下载地址
	 */
	private transient String downloadUrl;
	private String downloadPath;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
}
