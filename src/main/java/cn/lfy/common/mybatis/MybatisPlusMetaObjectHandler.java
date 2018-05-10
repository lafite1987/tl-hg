package cn.lfy.common.mybatis;

import org.apache.ibatis.reflection.MetaObject;
import org.springframework.stereotype.Component;

import com.baomidou.mybatisplus.mapper.MetaObjectHandler;

/**
 * 自定义填充处理器
 */
@Component
public class MybatisPlusMetaObjectHandler extends MetaObjectHandler {

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
