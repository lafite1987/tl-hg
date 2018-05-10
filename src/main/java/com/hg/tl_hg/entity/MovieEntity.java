package com.hg.tl_hg.entity;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.annotations.TableLogic;
import com.baomidou.mybatisplus.annotations.TableName;
import com.baomidou.mybatisplus.enums.FieldFill;
import com.baomidou.mybatisplus.enums.IdType;

import lombok.Data;

@TableName("t_movie")
@Data
public class MovieEntity {

	@TableId(value="id",type=IdType.AUTO)
    private Long id;
	
	private String title;
	
	private String content;
	
	private String coverUrl;
	
	private String videoUrl;
	
	private Integer praises;
	/**
	 * 0-初始；1-待审核；2-审核中；3-审核通过
	 */
	private Integer state;
	
	@TableField(fill = FieldFill.INSERT, value="createTime")
	private Long createTime;
	
	@TableField(fill = FieldFill.INSERT_UPDATE, value="updateTime")
	private Long updateTime;
	@TableLogic
    private Integer deleted;

}
