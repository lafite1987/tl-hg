package com.hg.tl_hg.ctrl.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hg.tl_hg.entity.VersionEntity;
import com.hg.tl_hg.service.VersionService;

import cn.lfy.common.Result;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class VersionRest {

	@Autowired
	private VersionService versionService;
	
	/**
	 * 检查更新
	 * @param type：1-IOS；2-Android；
	 * @return
	 */
	@RequestMapping(value = "/checkUpdate")
	@ApiOperation(value = "检查更新", httpMethod = "GET", notes = "检查更新接口")
	@ApiImplicitParams(@ApiImplicitParam(name = "type", value = "类型：1-IOS；2-Android", dataType = "Integer"))
	public @ResponseBody Result<VersionEntity> checkUpdate(
			@RequestParam(name = "type", required = true)Integer type,
			@RequestParam(name = "versionCode", required = true)Integer versionCode) {
		Result<VersionEntity> result = Result.success();
		VersionEntity entity = new VersionEntity();
		entity.setType(type);
		EntityWrapper<VersionEntity> wrapper = new EntityWrapper<VersionEntity>();
		wrapper.orderBy("versionCode", false);
		VersionEntity versionEntity = versionService.selectOne(wrapper);
		result.setData(versionEntity);
		return result;
	}
}
