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
import com.hg.tl_hg.entity.AdvertisementEntity;
import com.hg.tl_hg.service.AdvertisementService;

import cn.lfy.common.PageWrapper;
import cn.lfy.common.RestRequest;
import cn.lfy.common.Result;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/manager/advertisement")
public class AdvertisementCtrl {

	@Autowired
	private AdvertisementService advertisementService;
	
	@Autowired
	private FileServerConfig fileServerConfig;
	
	@RequestMapping(value = "/list.json")
	@ApiOperation(value = "列表", httpMethod = "GET", notes = "列表接口")
	public @ResponseBody Result<PageWrapper<AdvertisementEntity>> list(@RequestBody RestRequest<AdvertisementEntity> restRequest) {
		Result<PageWrapper<AdvertisementEntity>> result = Result.success();
		EntityWrapper<AdvertisementEntity> wrapper = new EntityWrapper<AdvertisementEntity>(restRequest.getQuery());
		Page<AdvertisementEntity> page = advertisementService.selectPage(restRequest.toPage(), wrapper);
		for(AdvertisementEntity entity : page.getRecords()) {
			entity.setImageUrl(fileServerConfig.getDomain() + entity.getImagePath());
		}
		PageWrapper<AdvertisementEntity> pageWrapper = PageWrapper.buildPageWrapper(page);
		result.setData(pageWrapper);
		return result;
	}

	@RequestMapping(value = "/{id}/del.json", method = RequestMethod.POST)
	@ApiOperation(value = "删除", httpMethod = "POST", notes = "删除")
	public @ResponseBody Result<Boolean> del(@PathVariable(name = "id") Long id) {
		advertisementService.deleteById(id);
		Result<Boolean> result = Result.success();
		return result;
	}

	@RequestMapping(value = "/{id}/detail.json", method = RequestMethod.GET)
	@ApiOperation(value = "获取详情", httpMethod = "GET", notes = "获取详情")
	public @ResponseBody Result<AdvertisementEntity> detail(@PathVariable(name = "id") Long id) {
		Result<AdvertisementEntity> result = Result.success();
		AdvertisementEntity entity = advertisementService.selectById(id);
		entity.setImageUrl(fileServerConfig.getDomain() + entity.getImagePath());
		result.setData(entity);
		return result;
	}

	@RequestMapping(value = "/add.json")
	@ApiOperation(value = "新增", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "新增")
	public @ResponseBody Result<Void> add(@RequestBody AdvertisementEntity form) {
		Result<Void> result = Result.success();
		advertisementService.insert(form);
		return result;
	}

	@RequestMapping(value = "/{id}/update.json")
	@ResponseBody
	@ApiOperation(value = "更新", httpMethod = "POST", notes = "更新用户")
	public Result<Void> update(@RequestBody AdvertisementEntity form) {
		Result<Void> result = Result.success();
		advertisementService.updateById(form);
		return result;
	}
}
