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
import com.hg.tl_hg.entity.RegistrationCodeEntity;
import com.hg.tl_hg.service.RegistrationCodeService;

import cn.lfy.common.PageWrapper;
import cn.lfy.common.RestRequest;
import cn.lfy.common.Result;
import io.swagger.annotations.ApiOperation;

@Controller
@RequestMapping("/manager/registrationCode")
public class RegistrationCodeCtrl {

	@Autowired
	private RegistrationCodeService registrationCodeService;
	
	@RequestMapping(value = "/list.json")
	@ApiOperation(value = "列表", httpMethod = "GET", notes = "列表接口")
	public @ResponseBody Result<PageWrapper<RegistrationCodeEntity>> list(@RequestBody RestRequest<RegistrationCodeEntity> restRequest) {
		Result<PageWrapper<RegistrationCodeEntity>> result = Result.success();
		EntityWrapper<RegistrationCodeEntity> wrapper = new EntityWrapper<RegistrationCodeEntity>(restRequest.getQuery());
		Page<RegistrationCodeEntity> page = registrationCodeService.selectPage(restRequest.toPage(), wrapper);
		PageWrapper<RegistrationCodeEntity> pageWrapper = PageWrapper.buildPageWrapper(page);
		result.setData(pageWrapper);
		return result;
	}

	@RequestMapping(value = "/{id}/del.json", method = RequestMethod.POST)
	@ApiOperation(value = "删除", httpMethod = "POST", notes = "删除")
	public @ResponseBody Result<Boolean> del(@PathVariable(name = "id") Long id) {
		registrationCodeService.deleteById(id);
		Result<Boolean> result = Result.success();
		return result;
	}

	@RequestMapping(value = "/{id}/detail.json", method = RequestMethod.GET)
	@ApiOperation(value = "获取详情", httpMethod = "GET", notes = "获取详情")
	public @ResponseBody Result<RegistrationCodeEntity> detail(@PathVariable(name = "id") Long id) {
		Result<RegistrationCodeEntity> result = Result.success();
		RegistrationCodeEntity entity = registrationCodeService.selectById(id);
		result.setData(entity);
		return result;
	}

	@RequestMapping(value = "/add.json")
	@ApiOperation(value = "新增", httpMethod = "POST", consumes = MediaType.APPLICATION_JSON_UTF8_VALUE, notes = "新增")
	public @ResponseBody Result<Void> add(@RequestBody RegistrationCodeEntity form) {
		Result<Void> result = Result.success();
		registrationCodeService.insert(form);
		return result;
	}

	@RequestMapping(value = "/{id}/update.json")
	@ResponseBody
	@ApiOperation(value = "更新", httpMethod = "POST", notes = "更新用户")
	public Result<Void> update(@RequestBody RegistrationCodeEntity form) {
		Result<Void> result = Result.success();
		registrationCodeService.updateById(form);
		return result;
	}
}
