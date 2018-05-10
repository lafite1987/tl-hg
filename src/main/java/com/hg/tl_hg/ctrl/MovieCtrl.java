package com.hg.tl_hg.ctrl;

import java.util.List;

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
import com.hg.tl_hg.entity.MovieEntity;
import com.hg.tl_hg.service.MovieService;

import cn.lfy.common.PageWrapper;
import cn.lfy.common.Query;
import cn.lfy.common.Result;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/movie")
public class MovieCtrl {

	@Autowired
	private MovieService movieService;
	
	@RequestMapping(value = "/list.json")
	@ApiOperation(value = "列表", httpMethod = "GET", notes = "列表接口")
	public @ResponseBody Result<PageWrapper<MovieEntity>> list(@RequestBody Query<MovieEntity> query) {
		Result<PageWrapper<MovieEntity>> result = Result.success();
		EntityWrapper<MovieEntity> wrapper = new EntityWrapper<MovieEntity>(query.getQuery());
		Page<MovieEntity> page = movieService.selectPage(query.toPage(), wrapper);
		PageWrapper<MovieEntity> pageWrapper = PageWrapper.buildPageWrapper(page.getCurrent(), page.getSize(), page.getTotal());
		pageWrapper.setList(page.getRecords());
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
		result.setData(entity);
		return result;
	}

	@RequestMapping(value = "/add.json")
	@ApiOperation(value = "新增", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "新增")
	public @ResponseBody Result<Void> add(@RequestBody MovieEntity form) {
		Result<Void> result = Result.success();
		movieService.insert(form);
		return result;
	}

	@RequestMapping(value = "/{id}/update.json")
	@ResponseBody
	@ApiOperation(value = "更新", httpMethod = "POST", notes = "更新用户")
	public Result<Void> update(@RequestBody MovieEntity form) {
		Result<Void> result = Result.success();
		movieService.updateById(form);
		return result;
	}
	
	@RequestMapping(value = "/recommend.json")
	@ApiOperation(value = "随机推荐", httpMethod = "GET", notes = "推荐接口")
	public @ResponseBody Result<List<MovieEntity>> recommend(int count) {
		Result<List<MovieEntity>> result = Result.success();
		EntityWrapper<MovieEntity> wrapper = new EntityWrapper<MovieEntity>();
		List<MovieEntity> list = movieService.selectList(wrapper);
		result.setData(list);
		return result;
	}
}
