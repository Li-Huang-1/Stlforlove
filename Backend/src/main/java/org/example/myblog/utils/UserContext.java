package org.example.myblog.utils;

import org.example.myblog.domain.entity.User;

/**
 * 用户上下文工具类
 * 作用：在当前线程中存储和获取当前登录用户信息
 */

// Tomcat自带线程池，但其最大线程数并不是一个固定值，而是一个可配置的参数，默认值通常是 200
public class UserContext {

    // ThreadLocal：每个线程独享一份数据
    private static final ThreadLocal<User> CURRENT_USER = new ThreadLocal<>();

    /**
     * 存储当前用户到线程局部变量
     */
    public static void setCurrentUser(User user) {
        CURRENT_USER.set(user);
    }

    /**
     * 获取当前线程中的用户
     * @return 当前登录用户，如果没有则返回 null
     */
    public static User getCurrentUser() {
        return CURRENT_USER.get();
    }

    /**
     * 清除当前线程的用户信息
     * 必须在请求结束后调用，防止内存泄漏
     */
    public static void clear() {
        CURRENT_USER.remove();
    }
}