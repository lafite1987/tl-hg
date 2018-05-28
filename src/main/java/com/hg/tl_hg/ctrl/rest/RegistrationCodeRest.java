package com.hg.tl_hg.ctrl.rest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.hg.tl_hg.entity.RegistrationCodeEntity;
import com.hg.tl_hg.service.RegistrationCodeService;

import cn.lfy.common.Result;
import cn.lfy.common.exception.ErrorCode;
import cn.lfy.common.exception.GlobalException;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/registrationCode")
public class RegistrationCodeRest {

	@Autowired
	private RegistrationCodeService registrationCodeService;
	
	@RequestMapping(value = "/login")
	@ApiOperation(value = "注册码登录", httpMethod = "GET", notes = "注册码登录接口")
	public @ResponseBody Result<RegistrationCodeEntity> login(
			@RequestParam(name = "id", required = true)Long id,
			@RequestParam(name = "code", required = true)String code) {
		Result<RegistrationCodeEntity> result = Result.success();
		RegistrationCodeEntity entity = new RegistrationCodeEntity();
		entity.setId(id);
		entity.setCode(code);
		EntityWrapper<RegistrationCodeEntity> wrapper = new EntityWrapper<RegistrationCodeEntity>(entity);
		RegistrationCodeEntity registrationCodeEntity = registrationCodeService.selectOne(wrapper);
		if(registrationCodeEntity == null) {
			throw GlobalException.newGlobalException(ErrorCode.REGISTER_CODE_ERROR);
		}
		result.setData(registrationCodeEntity);
		return result;
	}
}
