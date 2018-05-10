package cn.lfy.common;

import com.baomidou.mybatisplus.plugins.Page;

import lombok.Data;

@Data
public class RestRequest<T> {

	private PageInfo page = new PageInfo();
	private T query;
	
	public Page<T> toPage() {
		Page<T> page = new Page<>(this.page.getCurrentPage(), this.page.getPageSize());
		return page;
	}
}
