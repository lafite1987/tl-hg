package com.hg.tl_hg.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

/**
 * 广告
 * @author honeyleo
 *
 */
@TableName("t_advertisement")
@Data
public class AdvertisementEntity {

	@TableId(value="id",type=IdType.AUTO)
    private Long id;
	
	private String title;
	
	private String content;
	/**
	 * 链接
	 */
	private String link;
	
	private String imageUrl;
	
	private Integer state;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
}
