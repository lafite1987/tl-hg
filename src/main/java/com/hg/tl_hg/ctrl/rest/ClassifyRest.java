package com.hg.tl_hg.ctrl.rest;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hg.tl_hg.entity.ClassifyEntity;
import com.hg.tl_hg.service.ClassifyService;

import cn.lfy.common.Result;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/classify")
public class ClassifyRest {

	@Autowired
	private ClassifyService classifyService;
	
	@RequestMapping(value = "/list")
	@ApiOperation(value = "分类", httpMethod = "GET", notes = "分类接口")
	public @ResponseBody Result<List<ClassifyEntity>> list() {
		Result<List<ClassifyEntity>> result = Result.success();
		EntityWrapper<ClassifyEntity> wrapper = new EntityWrapper<ClassifyEntity>();
		wrapper.orderBy("sequence", true);
		List<ClassifyEntity> list = classifyService.selectList(wrapper);
		result.setData(list);
		return result;
	}
	
}
