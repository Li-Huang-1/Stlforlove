package org.example.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.myblog.domain.entity.User;

public interface IUserService extends IService<User> {

    /**
     * 用户登录
     */
    User login(String username, String password);

    /**
     * 根据Token获取用户信息
     */
    User getUserInfo(String token);
}
