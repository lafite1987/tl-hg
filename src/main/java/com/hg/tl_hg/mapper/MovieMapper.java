package com.hg.tl_hg.mapper;

import org.springframework.stereotype.Repository;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.hg.tl_hg.entity.MovieEntity;

@Repository
public interface MovieMapper extends BaseMapper<MovieEntity> {

	/**
	 * 添加PV
	 * @param id
	 * @return
	 */
	int addPv(Long id);
}
