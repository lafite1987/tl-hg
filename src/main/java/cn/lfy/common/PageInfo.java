package cn.lfy.common;

import lombok.Data;

@Data
public class PageInfo {

	private int currentPage = 1;
	private int pageSize = 20;
	private int totalNum;
	
	public PageInfo() {
		
	}
	public PageInfo(int currentPage, int pageSize, int totalNum) {
		this.currentPage = currentPage;
		this.pageSize = pageSize;
		this.totalNum = totalNum;
	}
}
