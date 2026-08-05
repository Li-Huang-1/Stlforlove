package org.example.myblog.config;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * .env 文件加载器
 * 在 Spring Boot 启动时加载 .env 文件中的环境变量
 */
public class DotenvConfig implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        // 获取 Spring 的环境管家
        ConfigurableEnvironment env = applicationContext.getEnvironment();

        try {
            // 尝试加载 .env 文件
            Dotenv dotenv = Dotenv.configure()
                    // 去当前项目根目录找
                    .directory("./")
                    // 找不到文件别报错，默默跳过
                    .ignoreIfMissing()
                    // 真正开始读取文件
                    .load();

            Map<String, Object> envMap = new HashMap<>();
            // .entries() 返回 .env 里所有键值对的集合
            // .forEach() （遍历），把每一对 KEY=VALUE 塞进 HashMap
            dotenv.entries().forEach(entry -> envMap.put(entry.getKey(), entry.getValue()));

            if (!envMap.isEmpty()) {
                // 把 Map 塞进 Spring 环境
                MapPropertySource propertySource = new MapPropertySource("dotenv", envMap);
                env.getPropertySources().addFirst(propertySource);
            }
        } catch (Exception e) {
            // .env 文件不存在时静默忽略
        }
    }
}
