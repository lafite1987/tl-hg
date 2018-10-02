package com.hg.tl_hg.service;

import com.baomidou.mybatisplus.service.IService;
import com.hg.tl_hg.entity.MovieEntity;

public interface MovieService extends IService<MovieEntity> {

	/**
	 * 添加PV
	 * @param id
	 * @return
	 */
	int addPv(Long id);
}
