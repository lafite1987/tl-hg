package com.hg.tl_hg.configure;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@ConfigurationProperties(prefix = "fileserver")
public class FileServerConfig {

    private String path;
    
    private String domain;
    
}
