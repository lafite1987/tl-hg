package com.hg.tl_hg.service;

import java.util.List;

import com.baomidou.mybatisplus.service.IService;
import com.hg.tl_hg.entity.MovieClassifyEntity;

public interface MovieClassifyService extends IService<MovieClassifyEntity> {

	List<Long> getClassifyIdListByMovieId(Long movieId);
	
	int insertBatch(Long movieId, List<Long> classifys);
	
	int delete(Long movieId, List<Long> classifys);
}
