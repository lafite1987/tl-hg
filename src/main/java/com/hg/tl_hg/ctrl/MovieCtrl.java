package com.hg.tl_hg.ctrl;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.common.collect.Sets.SetView;
import com.hg.tl_hg.configure.FileServerConfig;
import com.hg.tl_hg.entity.MovieEntity;
import com.hg.tl_hg.service.MovieClassifyService;
import com.hg.tl_hg.service.MovieService;

import cn.lfy.common.PageWrapper;
import cn.lfy.common.RestRequest;
import cn.lfy.common.Result;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/manager/movie")
public class MovieCtrl {

	@Autowired
	private MovieService movieService;
	
	@Autowired
	private MovieClassifyService movieClassifyService;
	
	@Autowired
	private FileServerConfig fileServerConfig;
	
	@RequestMapping(value = "/list.json")
	@ApiOperation(value = "列表", httpMethod = "GET", notes = "列表接口")
	public @ResponseBody Result<PageWrapper<MovieEntity>> list(@RequestBody RestRequest<MovieEntity> restRequest) {
		Result<PageWrapper<MovieEntity>> result = Result.success();
		EntityWrapper<MovieEntity> wrapper = new EntityWrapper<MovieEntity>(restRequest.getQuery());
		Page<MovieEntity> page = movieService.selectPage(restRequest.toPage(), wrapper);
		for(MovieEntity entity : page.getRecords()) {
			handleDomain(entity);
			List<Long> classifys = movieClassifyService.getClassifyIdListByMovieId(entity.getId());
			entity.setClassifys(classifys);
		}
		PageWrapper<MovieEntity> pageWrapper = PageWrapper.buildPageWrapper(page);
		result.setData(pageWrapper);
		return result;
	}

	@RequestMapping(value = "/{id}/del.json", method = RequestMethod.POST)
	@ApiOperation(value = "删除", httpMethod = "POST", notes = "删除")
	public @ResponseBody Result<Boolean> del(@PathVariable(name = "id") Long id) {
		movieService.deleteById(id);
		Result<Boolean> result = Result.success();
		return result;
	}

	@RequestMapping(value = "/{id}/detail.json", method = RequestMethod.GET)
	@ApiOperation(value = "获取详情", httpMethod = "GET", notes = "获取详情")
	public @ResponseBody Result<MovieEntity> detail(@PathVariable(name = "id") Long id) {
		Result<MovieEntity> result = Result.success();
		MovieEntity entity = movieService.selectById(id);
		List<Long> classifys = movieClassifyService.getClassifyIdListByMovieId(entity.getId());
		entity.setClassifys(classifys);
		result.setData(handleDomain(entity));
		return result;
	}

	private MovieEntity handleDomain(MovieEntity entity) {
		if(StringUtils.isNotBlank(entity.getCoverPath())) {
			entity.setCoverUrl(fileServerConfig.getDomain() + entity.getCoverPath());
		}
		if(StringUtils.isNotBlank(entity.getVideoPath())) {
			entity.setVideoUrl(fileServerConfig.getDomain() + entity.getVideoPath());
		}
		return entity;
	}
	@RequestMapping(value = "/add.json")
	@ApiOperation(value = "新增", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "新增")
	public @ResponseBody Result<Void> add(@RequestBody MovieEntity form) {
		Result<Void> result = Result.success();
		boolean ret = movieService.insert(form);
		if(ret) {
			movieClassifyService.insertBatch(form.getId(), form.getClassifys());
		}
		return result;
	}

	@RequestMapping(value = "/{id}/update.json")
	@ResponseBody
	@ApiOperation(value = "更新", httpMethod = "POST", notes = "更新用户")
	public Result<Void> update(@RequestBody MovieEntity form) {
		Result<Void> result = Result.success();
		List<Long> classifys = movieClassifyService.getClassifyIdListByMovieId(form.getId());
		Set<Long> nowClassifys = Sets.newHashSet(form.getClassifys());
		Set<Long> oldClassifys = Sets.newHashSet(classifys);
		SetView<Long> newClassifys = Sets.difference(nowClassifys, oldClassifys);
		movieClassifyService.insertBatch(form.getId(), Lists.newArrayList(newClassifys));
		SetView<Long> delClassifys = Sets.difference(oldClassifys, nowClassifys);
		movieClassifyService.delete(form.getId(), Lists.newArrayList(delClassifys));
		movieService.updateById(form);
		return result;
	}
}
