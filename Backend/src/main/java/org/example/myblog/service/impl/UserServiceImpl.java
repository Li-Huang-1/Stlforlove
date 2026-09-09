package org.example.myblog.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myblog.domain.entity.User;
import org.example.myblog.mapper.UserMapper;
import org.example.myblog.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements IUserService {

    @Autowired
    private UserMapper mapper;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public User login(String username, String password) {
        // 1. 构建查询条件:根据用户名查询用户(查出 password 字段用于 BCrypt 比对)
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getPassword)
                .eq(User::getUsername, username);

        User user = mapper.selectOne(queryWrapper);

        // 2. 用户不存在或密码错误
        // BCrypt 比对:明文 password 与数据库中的哈希做 matches 校验(盐值已内置在哈希里)
        if (user == null || !passwordEncoder.matches(password, user.getPassword())) {
            return null;
        }

        // 3. 登录成功,生成 Token 并保存到数据库
        String token = UUID.randomUUID().toString();
        User update = new User();
        update.setId(user.getId());
        update.setToken(token);
        mapper.updateById(update);

        // 清空密码,避免随返回值泄露到上层(VO 无 password 字段,这里做双保险)
        user.setPassword(null);
        user.setToken(token);
        return user;
    }

    @Override
    public User getUserInfo(String token) {
        // 构建 Token 查询条件(用于拦截器)
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getToken, User::getAvatar)
                .eq(User::getToken, token);

        return mapper.selectOne(queryWrapper);
    }
}
