package org.example.myblog.service.impl;

import org.example.myblog.domain.entity.User;
import org.example.myblog.mapper.UserMapper;
import org.example.myblog.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User login(String username, String password) {
        // 1. 根据用户名查询用户
        User user = userMapper.queryByUsername(username);

        // 2. 用户不存在或密码错误
        if (user == null || !user.getPassword().equals(password)) {
            return null;
        }

        // 3. 登录成功，生成 Token 并保存到数据库
        String token = UUID.randomUUID().toString();
        user.setToken(token);
        userMapper.updateToken(user);

        return user;
    }

    @Override
    public User getUserInfo(String token) {
        // 根据Token查询用户信息
        return userMapper.queryByToken(token);
    }
}