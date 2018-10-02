package com.hg.tl_hg.ctrl.rest;

import java.util.Calendar;

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
import cn.lfy.common.util.JwtToken;
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
		EntityWrapper<RegistrationCodeEntity> wrapper = new EntityWrapper<RegistrationCodeEntity>(entity);
		RegistrationCodeEntity registrationCodeEntity = registrationCodeService.selectOne(wrapper);
		if(registrationCodeEntity == null) {
			throw GlobalException.newGlobalException(ErrorCode.REGISTER_CODE_ERROR);
		} else if(!code.equalsIgnoreCase(registrationCodeEntity.getCode())) {
			throw GlobalException.newGlobalException(ErrorCode.REGISTER_CODE_PASSWORD_ERROR);
		}
		else if(registrationCodeEntity.getState() == 1) {
			throw GlobalException.newGlobalException(ErrorCode.REGISTER_CODE_STOP);
		} else if(registrationCodeEntity.getActivateTime() == 0) {
			long activateTime = System.currentTimeMillis();
			registrationCodeEntity.setActivateTime(activateTime);
			Calendar calendar = Calendar.getInstance();
			calendar.add(Calendar.DAY_OF_MONTH, registrationCodeEntity.getDays());
			registrationCodeEntity.setExpireTime(calendar.getTimeInMillis());
			registrationCodeService.updateById(registrationCodeEntity);
		} else if(registrationCodeEntity.getExpireTime() < System.currentTimeMillis()) {
			throw GlobalException.newGlobalException(ErrorCode.REGISTER_CODE_EXPIRED);
		}
		String token = JwtToken.createToken(id);
		registrationCodeEntity.setToken(token);
		result.setData(registrationCodeEntity);
		return result;
	}
}
