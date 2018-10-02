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
	/**
	 * 图片相对路径
	 */
	private String imagePath;
	private transient String imageUrl;
	/**
	 * 广告类型：1-开机广告；2-首页广告；3-详情页上方广告；4-详情页大广告；5-首页轮播广告；6-详情页下方广告
	 */
	private Integer type;
	/**
	 * 审核状态：1-创建；2-待审核；3-审核通过；4-审核失败
	 */
	private Integer auditStatus;
	/**
	 * 1-未开始投放；2-正在投放；3-暂停投放；4-停止；
	 */
	private Integer state;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	
	@TableLogic
    private Integer deleted;
}
