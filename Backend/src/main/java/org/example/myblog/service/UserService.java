package org.example.myblog.service;

import org.example.myblog.domain.entity.User;

public interface UserService {
    /**
     * 用户登录
     * @param username
     * @param password
     * @return
     */
    User login(String username, String password);
    
    /**
     * 根据Token获取用户信息
     * @param token 用户Token
     * @return 用户信息
     */
    User getUserInfo(String token);
}
