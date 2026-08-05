package org.example.myblog.config;

import org.example.myblog.interceptor.TokenInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Spring MVC 配置类
 * 作用：注册拦截器、配置跨域、静态资源等
 *
 * 当前配置：
 * - 注册 TokenInterceptor 拦截器
 * - 拦截所有请求（/**）
 * - 放行登录、注册等不需要 Token 的接口
 * - 配置跨域（CORS）
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;

    /**
     * 配置跨域（CORS）
     * 允许前端（如 http://localhost:8080）访问后端接口
     */
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                // 允许所有来源（开发环境）
                .allowedOriginPatterns("*")
                // 允许的 HTTP 方法
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                // 允许的请求头
                .allowedHeaders("*")
                // 是否允许携带凭证（如 Cookie）
                .allowCredentials(true)
                // 预检请求的缓存时间（秒）
                .maxAge(3600);
    }

    /**
     * 注册拦截器
     * 告诉 Spring：
     * 1. .addInterceptor (用哪个拦截器 → tokenInterceptor)
     * 2. .addPathPatterns (拦哪些请求 → /admin/**（secure所有请求）)
     * 3. .excludePathPatterns (不拦哪些请求 → /public/**)
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(tokenInterceptor)
                .addPathPatterns("/admin/**")
                .excludePathPatterns("/public/**", "/uploads/**");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:./uploads/");
    }
}