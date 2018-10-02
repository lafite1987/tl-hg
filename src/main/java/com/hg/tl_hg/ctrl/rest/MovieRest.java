package com.hg.tl_hg.ctrl.rest;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.hg.tl_hg.configure.FileServerConfig;
import com.hg.tl_hg.entity.ClassifyEntity;
import com.hg.tl_hg.entity.MovieClassifyEntity;
import com.hg.tl_hg.entity.MovieEntity;
import com.hg.tl_hg.service.ClassifyService;
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
	
	@Autowired
	private ClassifyService clasifyService;
	
	@Autowired
	private FileServerConfig fileServerConfig;
	
	@RequestMapping(value = "/index/{classifyId}")
	@ApiOperation(value = "首页", httpMethod = "GET", notes = "首页接口")
	public @ResponseBody Result<PageWrapper<MovieEntity>> index(
			@PathVariable(name = "classifyId", required = true) Long classifyId,
			PageInfo pageInfo) {
		ClassifyEntity classifyEntity = clasifyService.selectById(classifyId);
		String classifyName = "最新";
		if(classifyEntity != null) {
			classifyName = classifyEntity.getName();
		}
		switch (classifyName) {
			case "最新" :
				return lastest(classifyId, pageInfo);
			case "最热" :
				return hotest(classifyId, pageInfo);
			default :
				return defaultData(classifyId, pageInfo);
		}
	}
	
	private Result<PageWrapper<MovieEntity>> lastest(Long classifyId, PageInfo pageInfo) {
		Result<PageWrapper<MovieEntity>> result = Result.success();
		Page<MovieEntity> page = new Page<>(pageInfo.getCurrentPage(), pageInfo.getPageSize());
		MovieEntity entity = new MovieEntity();
		entity.setState(3);
		EntityWrapper<MovieEntity> wrapper = new EntityWrapper<MovieEntity>(entity);
		wrapper.orderBy("updateTime DESC, pv", false);
		Page<MovieEntity> moviePage = movieService.selectPage(page, wrapper);
		PageWrapper<MovieEntity> pageWrapper = PageWrapper.buildPageWrapper(moviePage.getCurrent(), moviePage.getSize(), moviePage.getTotal());
		for(MovieEntity movie : moviePage.getRecords()) {
			handleDomain(movie);
		}
		pageWrapper.setList(moviePage.getRecords());
		result.setData(pageWrapper);
		return result;
	}
	
	private Result<PageWrapper<MovieEntity>> hotest(Long classifyId, PageInfo pageInfo) {
		Result<PageWrapper<MovieEntity>> result = Result.success();
		Page<MovieEntity> page = new Page<>(pageInfo.getCurrentPage(), pageInfo.getPageSize());
		MovieEntity entity = new MovieEntity();
		entity.setState(3);
		EntityWrapper<MovieEntity> wrapper = new EntityWrapper<MovieEntity>(entity);
		wrapper.orderBy("pv DESC, updateTime", false);
		Page<MovieEntity> moviePage = movieService.selectPage(page, wrapper);
		PageWrapper<MovieEntity> pageWrapper = PageWrapper.buildPageWrapper(moviePage.getCurrent(), moviePage.getSize(), moviePage.getTotal());
		for(MovieEntity movie : moviePage.getRecords()) {
			handleDomain(movie);
		}
		pageWrapper.setList(moviePage.getRecords());
		result.setData(pageWrapper);
		return result;
	}
	private Result<PageWrapper<MovieEntity>> defaultData(Long classifyId, PageInfo pageInfo) {
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
			for(MovieEntity entity : list) {
				handleDomain(entity);
			}
			pageWrapper.setList(list);
		}
		result.setData(pageWrapper);
		return result;
	}
	
	private MovieEntity handleDomain(MovieEntity entity) {
		if(entity == null) {
			return null;
		}
		if(StringUtils.isNotBlank(entity.getCoverPath())) {
			entity.setCoverUrl(fileServerConfig.getDomain() + entity.getCoverPath());
		}
		if(StringUtils.isNotBlank(entity.getVideoPath())) {
			entity.setVideoUrl(fileServerConfig.getDomain() + entity.getVideoPath());
		}
		return entity;
	}
	
	@RequestMapping(value = "/{id}/detail", method = RequestMethod.GET)
	@ApiOperation(value = "获取详情", httpMethod = "GET", notes = "获取详情")
	public @ResponseBody Result<MovieEntity> detail(@PathVariable(name = "id") Long id) {
		Result<MovieEntity> result = Result.success();
		MovieEntity entity = movieService.selectById(id);
		handleDomain(entity);
		result.setData(entity);
		movieService.addPv(id);
		return result;
	}
	
	@RequestMapping(value = "/recommend")
	@ApiOperation(value = "视频推荐接口", httpMethod = "GET", notes = "视频推荐接口")
	public @ResponseBody Result<List<MovieEntity>> recommend(@RequestParam(name = "size", defaultValue = "1") int size) {
		Result<List<MovieEntity>> result = Result.success();
		MovieEntity entity = new MovieEntity();
		EntityWrapper<MovieEntity> wrapper = new EntityWrapper<MovieEntity>(entity);
		wrapper.orderBy("updateTime", false);
		wrapper.orderBy("rand()");
		wrapper.last("limit " + size);
		List<MovieEntity> list = movieService.selectList(wrapper);
		for(MovieEntity en : list) {
			handleDomain(en);
		}
		result.setData(list);
		return result;
	}
	
}
