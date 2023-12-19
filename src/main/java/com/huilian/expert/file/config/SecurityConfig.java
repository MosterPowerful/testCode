package com.huilian.expert.file.config;


import com.huilian.expert.common.handler.RestAuthenticationEntryPoint;
import com.huilian.expert.common.handler.RestfulAccessDeniedHandler;
import com.huilian.expert.file.filter.AdminCookieFilter;
import com.huilian.expert.file.filter.ClientJwtFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.RequestCacheConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.Collections;

/**
 * 安全配置
 *
 * @author 赖卓成
 * @date 2023/02/27
 */
@EnableWebSecurity(debug = true)
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private RestAuthenticationEntryPoint restAuthenticationEntryPoint;

    @Autowired
    private RestfulAccessDeniedHandler restfulAccessDeniedHandler;

    @Autowired
    private AdminCookieFilter adminCookieFilter;

    @Autowired
    private ClientJwtFilter clientJwtFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .requestMatchers(req -> req.antMatchers("/**")).exceptionHandling(exp ->
                        exp
                                .authenticationEntryPoint(restAuthenticationEntryPoint)
                                .accessDeniedHandler(restfulAccessDeniedHandler)
                )

                .authorizeRequests(req -> req
                        /*.antMatchers("/static/**").permitAll()*/
                        .antMatchers("/file/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(adminCookieFilter, UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(clientJwtFilter,UsernamePasswordAuthenticationFilter.class)
                .formLogin(AbstractHttpConfigurer::disable)
                .requestCache(RequestCacheConfigurer::disable)
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(this.corsConfigurationSource())
                )
        ;
    }



    /**
     * 跨域配置
     *
     * @return {@link CorsConfigurationSource}
     */
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        // 配置对象
        CorsConfiguration configuration = new CorsConfiguration();
        // 允许跨域访问的主机

        configuration.setAllowedOrigins(Arrays.asList("http://localhost:8050", "https://localhost:8050", "http://wb.gxj.sz.gov.cn",
                "http://zhgx.gxj.sz.gov.cn", "https://wb.gxj.sz.gov.cn", "https://zhgx.gxj.sz.gov.cn","http://192.168.1.87:8050"
                ,"http://hlxx.w1.luyouxia.net", "https://hlxx.w1.luyouxia.net"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Collections.singletonList("*"));
        // 允许暴露的响应头 不设置在响应中是看不到的
        configuration.addExposedHeader("X-Authenticate");
        configuration.setAllowCredentials(true);
        // Bean
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers("/error/**","/swagger-ui.html",
                        "/swagger-ui/*",
                        "/swagger-resources/**",
                        "/v2/api-docs",
                        "/v3/api-docs")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
}
