package com.huilian.expert.file.config;

import com.huilian.expert.common.config.ApplicationProperties;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Web配置
 */
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {

    @Resource
    private ApplicationProperties applicationProperties;

    /**
     * 资源目录映射
     */
    @Override
    public void addResourceHandlers(@NotNull ResourceHandlerRegistry registry) {
        String path = applicationProperties.getFile().getPath();
        registry.addResourceHandler("/static/**")
                .addResourceLocations("file:"+path+"/");
    }

}
