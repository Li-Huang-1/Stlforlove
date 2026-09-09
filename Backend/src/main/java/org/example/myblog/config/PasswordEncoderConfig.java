package org.example.myblog.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * 密码加密配置
 *
 * 仅引入 spring-security-crypto 工具包,不引入 Spring Security 的安全过滤链。
 * 用 BCrypt 对用户密码做单向哈希(加盐),数据库只存哈希值,登录时用 matches 比对。
 */
@Configuration
public class PasswordEncoderConfig {

    /**
     * BCrypt 加密器
     * strength 即 cost 因子,取值 4~31,默认 10。10 表示 2^10 轮计算,够用且不卡。
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(10);
    }
}
