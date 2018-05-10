package cn.lfy.base.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "projectUrlConfig")
public class GlobalConfig {

    /**微信认证url.*/
    private String mpAuthorizeUrl;

    /**微信网页授权登录url.*/
    private String openAuthorizeUrl;

    /**点餐系统.*/
    private String xyct;

}
