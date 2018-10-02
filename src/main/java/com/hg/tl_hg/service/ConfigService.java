package com.hg.tl_hg.service;

import com.baomidou.mybatisplus.service.IService;
import com.hg.tl_hg.entity.ConfigEntity;

public interface ConfigService extends IService<ConfigEntity> {

	boolean isNeedRegistration();
}
