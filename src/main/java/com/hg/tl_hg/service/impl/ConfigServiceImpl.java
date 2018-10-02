package com.hg.tl_hg.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.hg.tl_hg.entity.ConfigEntity;
import com.hg.tl_hg.mapper.ConfigMapper;
import com.hg.tl_hg.service.ConfigService;

@Service
public class ConfigServiceImpl extends ServiceImpl<ConfigMapper,ConfigEntity> implements ConfigService {

	@Override
	public boolean isNeedRegistration() {
		ConfigEntity entity = new ConfigEntity();
		entity.setGroup("config");
		entity.setKey("registration.onOff");
		EntityWrapper<ConfigEntity> wrapper = new EntityWrapper<ConfigEntity>(entity);
		ConfigEntity one = this.selectOne(wrapper);
		if(one == null) {
			return true;
		}
		String value = one.getValue();
		if(StringUtils.isNotBlank(value) && "on".equalsIgnoreCase(value)) {
			return false;
		}
		return true;
	}

}
