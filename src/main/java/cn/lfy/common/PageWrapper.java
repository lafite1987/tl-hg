package cn.lfy.common;

import java.util.ArrayList;
import java.util.List;

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
}
