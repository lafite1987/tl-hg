package cn.lfy.common;

import java.util.ArrayList;
import java.util.List;

import com.baomidou.mybatisplus.plugins.Page;

import lombok.Data;

@Data
public class PageWrapper<T> {

	private PageInfo page;
	
	private List<T> list = new ArrayList<T>();
	
	public static <T> PageWrapper<T> buildPageWrapper(int currentPage, int pageSize, int totalNum) {
		PageInfo page = new PageInfo(currentPage, pageSize, totalNum);
		PageWrapper<T> pageWrapper = new PageWrapper<>();
		pageWrapper.page = page;
		return pageWrapper;
	}
	
	public static <T> PageWrapper<T> buildPageWrapper(Page<T> page) {
		PageInfo pageInfo = new PageInfo(page.getCurrent(), page.getSize(), page.getTotal());
		PageWrapper<T> pageWrapper = new PageWrapper<>();
		pageWrapper.page = pageInfo;
		pageWrapper.list = page.getRecords();
		return pageWrapper;
	}
}
