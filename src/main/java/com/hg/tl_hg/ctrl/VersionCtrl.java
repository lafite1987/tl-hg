package com.hg.tl_hg.ctrl;

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
import com.hg.tl_hg.configure.FileServerConfig;
import com.hg.tl_hg.entity.VersionEntity;
import com.hg.tl_hg.service.VersionService;

import cn.lfy.common.PageWrapper;
import cn.lfy.common.RestRequest;
import cn.lfy.common.Result;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/manager/version")
public class VersionCtrl {

	@Autowired
	private VersionService versionService;
	
	@Autowired
	private FileServerConfig fileServerConfig;
	
	@RequestMapping(value = "/list.json")
	@ApiOperation(value = "列表", httpMethod = "GET", notes = "列表接口")
	public @ResponseBody Result<PageWrapper<VersionEntity>> list(@RequestBody RestRequest<VersionEntity> restRequest) {
		Result<PageWrapper<VersionEntity>> result = Result.success();
		EntityWrapper<VersionEntity> wrapper = new EntityWrapper<VersionEntity>(restRequest.getQuery());
		Page<VersionEntity> page = versionService.selectPage(restRequest.toPage(), wrapper);
		for(VersionEntity entity : page.getRecords()) {
			entity.setImageUrl(fileServerConfig.getDomain() + entity.getImagePath());
			entity.setDownloadUrl(fileServerConfig.getDomain() + entity.getDownloadPath());
		}
		PageWrapper<VersionEntity> pageWrapper = PageWrapper.buildPageWrapper(page);
		result.setData(pageWrapper);
		return result;
	}

	@RequestMapping(value = "/{id}/del.json", method = RequestMethod.POST)
	@ApiOperation(value = "删除", httpMethod = "POST", notes = "删除")
	public @ResponseBody Result<Boolean> del(@PathVariable(name = "id") Long id) {
		versionService.deleteById(id);
		Result<Boolean> result = Result.success();
		return result;
	}

	@RequestMapping(value = "/{id}/detail.json", method = RequestMethod.GET)
	@ApiOperation(value = "获取详情", httpMethod = "GET", notes = "获取详情")
	public @ResponseBody Result<VersionEntity> detail(@PathVariable(name = "id") Long id) {
		Result<VersionEntity> result = Result.success();
		VersionEntity entity = versionService.selectById(id);
		entity.setImageUrl(fileServerConfig.getDomain() + entity.getImagePath());
		entity.setDownloadUrl(fileServerConfig.getDomain() + entity.getDownloadPath());
		result.setData(entity);
		return result;
	}

	@RequestMapping(value = "/add.json")
	@ApiOperation(value = "新增", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "新增")
	public @ResponseBody Result<Void> add(@RequestBody VersionEntity form) {
		Result<Void> result = Result.success();
		versionService.insert(form);
		return result;
	}

	@RequestMapping(value = "/{id}/update.json")
	@ResponseBody
	@ApiOperation(value = "更新", httpMethod = "POST", notes = "更新用户")
	public Result<Void> update(@RequestBody VersionEntity form) {
		Result<Void> result = Result.success();
		versionService.updateById(form);
		return result;
	}
}
