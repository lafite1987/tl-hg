package com.hg.tl_hg.configure;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.baomidou.mybatisplus.plugins.PaginationInterceptor;

@Configuration
@MapperScan("com.hg.tl_hg.mapper") 
public class MybatisPlusConfig {
    /**
     * mybatis-plus 分页插件
     */
    @Bean
    public PaginationInterceptor paginationInterceptor(){
        PaginationInterceptor page = new PaginationInterceptor();
        page.setDialectType("mysql");
        return page;
    }
    
//    @Autowired
//    private MyMetaObjectHandler myMetaObjectHandler;
//    
//    @Bean
//    public GlobalConfiguration globalConfig() {
//        GlobalConfiguration conf = new GlobalConfiguration(new LogicSqlInjector());
//        conf.setLogicDeleteValue("1");
//        conf.setLogicNotDeleteValue("0");
//        conf.setIdType(0);
//        conf.setMetaObjectHandler(myMetaObjectHandler);
//        return conf;
//    }

}
