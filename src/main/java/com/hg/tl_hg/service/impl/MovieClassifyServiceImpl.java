package com.hg.tl_hg.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.google.common.collect.Lists;
import com.hg.tl_hg.entity.MovieClassifyEntity;
import com.hg.tl_hg.mapper.MovieClassifyMapper;
import com.hg.tl_hg.service.MovieClassifyService;

@Service
public class MovieClassifyServiceImpl extends ServiceImpl<MovieClassifyMapper,MovieClassifyEntity> implements MovieClassifyService {

	@Override
	public List<Long> getClassifyIdListByMovieId(Long movieId) {
		MovieClassifyEntity entity = new MovieClassifyEntity();
		entity.setMovieId(movieId);
		EntityWrapper<MovieClassifyEntity> wrapper = new EntityWrapper<MovieClassifyEntity>(entity);
		List<MovieClassifyEntity> list = this.baseMapper.selectList(wrapper);
		List<Long> classifyIdList = Lists.newArrayList();
		for(MovieClassifyEntity classify : list) {
			classifyIdList.add(classify.getClassifyId());
		}
		return classifyIdList;
	}

	@Override
	public int insertBatch(Long movieId, List<Long> classifys) {
		List<MovieClassifyEntity> list = Lists.newArrayList();
		for(Long classifyId : classifys) {
			MovieClassifyEntity movieClassifyEntity = new MovieClassifyEntity();
			movieClassifyEntity.setClassifyId(classifyId);
			movieClassifyEntity.setMovieId(movieId);
			list.add(movieClassifyEntity);
		}
		if(!list.isEmpty()) {
			this.insertBatch(list);
		}
		return classifys.size();
	}

	@Override
	public int delete(Long movieId, List<Long> classifys) {
		if(!classifys.isEmpty()) {
			MovieClassifyEntity entity = new MovieClassifyEntity();
			entity.setMovieId(movieId);
			EntityWrapper<MovieClassifyEntity> wrapper = new EntityWrapper<>();
			wrapper.in("classifyId", classifys);
			boolean ret = this.delete(wrapper);
			return ret ? classifys.size() : 0;
		}
		return 0;
	}

}
