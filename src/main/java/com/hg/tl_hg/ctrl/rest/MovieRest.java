package com.hg.tl_hg.ctrl.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.hg.tl_hg.entity.MovieClassifyEntity;
import com.hg.tl_hg.entity.MovieEntity;
import com.hg.tl_hg.service.MovieClassifyService;
import com.hg.tl_hg.service.MovieService;

import cn.lfy.common.PageInfo;
import cn.lfy.common.PageWrapper;
import cn.lfy.common.Result;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/movie")
public class MovieRest {

	@Autowired
	private MovieClassifyService movieClassifyService;
	
	@Autowired
	private MovieService movieService;
	
	@RequestMapping(value = "/index/{classifyId}")
	@ApiOperation(value = "首页", httpMethod = "GET", notes = "首页接口")
	public @ResponseBody Result<PageWrapper<MovieEntity>> index(
			@PathVariable(name = "classifyId", required = true) Long classifyId,
			PageInfo pageInfo) {
		Result<PageWrapper<MovieEntity>> result = Result.success();
		MovieClassifyEntity movieClassifyEntity = new MovieClassifyEntity();
		movieClassifyEntity.setClassifyId(classifyId);
		EntityWrapper<MovieClassifyEntity> wrapper = new EntityWrapper<MovieClassifyEntity>(movieClassifyEntity);
		wrapper.orderBy("updateTime", false);
		Page<MovieClassifyEntity> page = new Page<>(pageInfo.getCurrentPage(), pageInfo.getPageSize());
		Page<MovieClassifyEntity> page2 = movieClassifyService.selectPage(page, wrapper);
		PageWrapper<MovieEntity> pageWrapper = PageWrapper.buildPageWrapper(page2.getCurrent(), page2.getSize(), page2.getTotal());
		List<Long> movieIdList = Lists.newArrayList();
		if(!page2.getRecords().isEmpty()) {
			for(MovieClassifyEntity movieClassify : page2.getRecords()) {
				movieIdList.add(movieClassify.getMovieId());
			}
			List<MovieEntity> list = movieService.selectBatchIds(movieIdList);
			pageWrapper.setList(list);
		}
		result.setData(pageWrapper);
		return result;
	}
	
	@RequestMapping(value = "/{id}/detail", method = RequestMethod.GET)
	@ApiOperation(value = "获取详情", httpMethod = "GET", notes = "获取详情")
	public @ResponseBody Result<MovieEntity> detail(@PathVariable(name = "id") Long id) {
		Result<MovieEntity> result = Result.success();
		MovieEntity entity = movieService.selectById(id);
		result.setData(entity);
		return result;
	}
}
