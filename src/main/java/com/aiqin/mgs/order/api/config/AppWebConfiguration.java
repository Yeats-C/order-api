package com.aiqin.mgs.order.api.config;

import com.aiqin.mgs.order.api.intercepter.UrlInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import javax.annotation.Resource;

/**
 * Created by Administrator on 2018/8/28.
 */
@Component
public class AppWebConfiguration implements WebMvcConfigurer {

    @Resource
    private UrlInterceptor urlInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(urlInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/doc.html",
                        "/swagger-resources",
                        "/webjars/**",
                        "/init/**",
                        "/order/**",
                        "/orderdetail/**",
                        "/orderafter/**",
                        "/frozen/**",
                        "/service/**",
                        "/orderNoCode/**",
                        "/settlement/**",
                        "/erpOrderSignController/**",
                        "/store-value/**",
                        "/payment/**"
                        );
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOrigins("*")
                .allowCredentials(true)
                .allowedMethods("GET", "POST", "DELETE", "PUT", "PATCH")
                .maxAge(3600);
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("swagger-ui.html")
                .addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**")
                .addResourceLocations("classpath:/META-INF/resources/webjars/");
    }
}