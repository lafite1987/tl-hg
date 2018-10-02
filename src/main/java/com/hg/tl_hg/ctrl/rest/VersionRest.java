package com.hg.tl_hg.ctrl.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hg.tl_hg.configure.FileServerConfig;
import com.hg.tl_hg.entity.VersionEntity;
import com.hg.tl_hg.service.ConfigService;
import com.hg.tl_hg.service.VersionService;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api")
public class VersionRest {

	@Autowired
	private VersionService versionService;
	
	@Autowired
	private FileServerConfig fileServerConfig;
	
	@Autowired
	private ConfigService configService;
	
	/**
	 * 检查更新
	 * @param type：1-IOS；2-Android；
	 * @return
	 */
	@RequestMapping(value = "/checkUpdate")
	@ApiOperation(value = "检查更新", httpMethod = "GET", notes = "检查更新接口")
	@ApiImplicitParams(@ApiImplicitParam(name = "type", value = "类型：1-IOS；2-Android", dataType = "Integer"))
	public @ResponseBody Object checkUpdate(
			@RequestParam(name = "type", required = true)Integer type,
			@RequestParam(name = "versionCode", required = true, defaultValue = "0")Integer versionCode) {
		VersionEntity entity = new VersionEntity();
		entity.setType(type);
		EntityWrapper<VersionEntity> wrapper = new EntityWrapper<VersionEntity>();
		wrapper.gt("versionCode", versionCode);
		wrapper.orderBy("versionCode", false);
		VersionEntity versionEntity = versionService.selectOne(wrapper);
		if(versionEntity != null) {
			versionEntity.setImageUrl(fileServerConfig.getDomain() + versionEntity.getImagePath());
			versionEntity.setDownloadUrl(fileServerConfig.getDomain() + versionEntity.getDownloadPath());
		}
		JSONObject json = new JSONObject();
		json.put("code", 200);
		json.put("message", "");
		json.put("redirect", "");
		json.put("data", versionEntity);
		json.put("needRegistration", configService.isNeedRegistration());
		return json;
	}
}
