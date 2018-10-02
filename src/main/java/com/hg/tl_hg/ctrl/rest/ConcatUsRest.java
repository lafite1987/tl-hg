package com.hg.tl_hg.ctrl.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hg.tl_hg.entity.ConfigEntity;
import com.hg.tl_hg.service.ConfigService;

import cn.lfy.common.Result;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/concatUs")
public class ConcatUsRest {

	@Autowired
	private ConfigService configService;
	
	@RequestMapping(value = "/info")
	@ApiOperation(value = "联系我们", httpMethod = "GET", notes = "联系我们接口")
	public @ResponseBody Result<ConfigEntity> info() {
		Result<ConfigEntity> result = Result.success();
		ConfigEntity condition = new ConfigEntity();
		condition.setGroup("concat");
		condition.setKey("us");
		EntityWrapper<ConfigEntity> wrapper = new EntityWrapper<ConfigEntity>(condition);
		
		ConfigEntity entity = configService.selectOne(wrapper);
		result.setData(entity);
		return result;
	}
}
