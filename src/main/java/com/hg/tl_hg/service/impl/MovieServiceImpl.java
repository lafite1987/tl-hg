package com.hg.tl_hg.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hg.tl_hg.entity.MovieEntity;
import com.hg.tl_hg.mapper.MovieMapper;
import com.hg.tl_hg.service.MovieService;

@Service
public class MovieServiceImpl extends ServiceImpl<MovieMapper,MovieEntity> implements MovieService {

	@Override
	public int addPv(Long id) {
		return this.baseMapper.addPv(id);
	}

}
