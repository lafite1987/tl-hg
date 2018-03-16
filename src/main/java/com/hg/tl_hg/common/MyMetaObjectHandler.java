package com.hg.tl_hg.common;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;

/**
 * 自定义填充处理器
 */
@Component
public class MyMetaObjectHandler extends MetaObjectHandler {

    @Override
    public void insertFill(MetaObject metaObject) {
        this.setFieldValByName("createTime", System.currentTimeMillis(), metaObject);
        this.setFieldValByName("updateTime", System.currentTimeMillis(), metaObject);
    }
    
    @Override
    public void updateFill(MetaObject metaObject) {
    	this.setFieldValByName("updateTime", System.currentTimeMillis(), metaObject);
    }
}
