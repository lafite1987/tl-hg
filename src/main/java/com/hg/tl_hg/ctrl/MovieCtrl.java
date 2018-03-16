package com.hg.tl_hg.ctrl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.plugins.Page;
import com.hg.tl_hg.common.PageBean;
import com.hg.tl_hg.common.Result;
import com.hg.tl_hg.entity.MovieEntity;
import com.hg.tl_hg.service.MovieService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/movie")
public class MovieCtrl {

	private static final Logger LOGGER = LoggerFactory.getLogger(MovieCtrl.class);
	
	@Autowired
	private MovieService movieService;
	
	@RequestMapping("/add")
	@ResponseBody
	@ApiOperation(value = "添加视频", httpMethod = "POST", notes = "添加视频接口")
	public Result<Boolean> add(@RequestBody MovieEntity form) {
		LOGGER.info("movie add {}", form);
		Result<Boolean> result = Result.<Boolean>success().setData(Boolean.TRUE);
		movieService.insert(form);
		return result;
	}
	
	@RequestMapping("/del/{id}")
	@ResponseBody
	@ApiOperation(value = "删除视频", httpMethod = "DELETE", notes = "删除接口")
	public Result<Boolean> del(@PathVariable Long id) {
		Result<Boolean> result = Result.<Boolean>success().setData(Boolean.TRUE);
		movieService.deleteById(id);
		return result;
	}
	
	@RequestMapping("/list")
	@ResponseBody
	@ApiOperation(value = "视频列表", httpMethod = "GET", notes = "视频列表接口")
	public Result<List<MovieEntity>> list(PageBean<MovieEntity> pageBean) {
		Result<List<MovieEntity>> result = Result.<List<MovieEntity>>success();
		Page<MovieEntity> page = movieService.selectPage(pageBean.toPage());
		result.setData(page.getRecords());
		return result;
	}
}
