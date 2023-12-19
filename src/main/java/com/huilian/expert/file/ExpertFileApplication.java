package com.huilian.expert.file;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;
import springfox.documentation.oas.annotations.EnableOpenApi;

/**
 * 专家文件应用程序
 *
 * @author 赖卓成
 * @date 2023/02/23
 */
@SpringBootApplication
@EnableOpenApi
@ComponentScan(basePackages = {"com.huilian.expert.file","com.huilian.expert.common"})
@MapperScan({"com.huilian.expert.file.mapper","com.huilian.expert.common.mapper"})
public class ExpertFileApplication extends SpringBootServletInitializer {
    public static void main(String[] args) {
        SpringApplication.run(ExpertFileApplication.class,args);
    }



    /**
     * 配置
     * 参考:https://docs.spring.io/spring-boot/docs/current/reference/htmlsingle/#howto.traditional-deployment.war
     *
     * @param application 应用程序
     * @return {@link SpringApplicationBuilder}
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ExpertFileApplication.class);
    }
}

