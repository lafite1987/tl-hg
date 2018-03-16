package com.hg.tl_hg.common;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import com.baomidou.mybatisplus.plugins.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class PageBean<T> {

	/**
     * 查询参数
     */
    private Map<String, Object> condition;
    
    /* 总数 */
    @ApiModelProperty(required = true)
    private int total;

    /* 每页显示条数，默认 10 */
    @ApiModelProperty(required = true)
    private int size = 10;

    /* 总页数 */
    private int pages;

    /* 当前页 */
    @ApiModelProperty(required = true)
    private int current = 1;
    
    @ApiModelProperty(required = true)
	private List<T> records = Collections.emptyList();

	public Map<String, Object> getCondition() {
		return condition;
	}

	public void setCondition(Map<String, Object> condition) {
		this.condition = condition;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getCurrent() {
		return current;
	}

	public void setCurrent(int current) {
		this.current = current;
	}

	public List<T> getRecords() {
		return records;
	}

	public void setRecords(List<T> records) {
		this.records = records;
	}
	
	public Page<T> toPage() {
		Page<T> page = new Page<>();
		page.setCurrent(current);
		page.setSize(size);
		page.setTotal(total);
		page.setCondition(condition);
		return page;
	}
}
