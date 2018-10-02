package cn.lfy.base.configure;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.validation.Validator;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import cn.lfy.base.interceptor.ApiInterceptor;
import cn.lfy.base.interceptor.SessionInterceptor;

@Configuration  
public class MvcConfigurer extends WebMvcConfigurerAdapter {  
  
//    @Override  
//    public void addViewControllers(ViewControllerRegistry registry) {  
//        registry.addViewController("/recharge.htm").setViewName("recharge.html");  
//        registry.setOrder(Ordered.HIGHEST_PRECEDENCE);  
//    }  
  
    @Override  
    public void configurePathMatch(PathMatchConfigurer configurer) {  
        super.configurePathMatch(configurer);  
        configurer.setUseSuffixPatternMatch(false);  
    }  
  
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
          registry.addResourceHandler("/webjars/**")
                 .addResourceLocations("classpath:/META-INF/resources/webjars/");
          registry.addResourceHandler("index.html")
          .addResourceLocations("classpath:/static/");
          registry.addResourceHandler("/static/**")
          .addResourceLocations("classpath:/static/static/");

    }
  
    @Autowired
	private SessionInterceptor sessionInterceptor;
    
    @Autowired
    private ApiInterceptor apiInterceptor;
	
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		registry.addInterceptor(sessionInterceptor).addPathPatterns("/manager/**");
		registry.addInterceptor(apiInterceptor).addPathPatterns("/api/**");
	}
	
	public ResourceBundleMessageSource getMessageSource() throws Exception {  
        ResourceBundleMessageSource rbms = new ResourceBundleMessageSource();  
        rbms.setDefaultEncoding("UTF-8");  
        rbms.setBasename("i18n/validation/ValidationMessages");
        return rbms;  
    }  
  
    @Bean  
    public Validator getValidator() {  
        LocalValidatorFactoryBean validator = new LocalValidatorFactoryBean();  
        try {
			validator.setValidationMessageSource(getMessageSource());
		} catch (Exception e) {
			e.printStackTrace();
		}  
        return validator;  
    }  
}  
