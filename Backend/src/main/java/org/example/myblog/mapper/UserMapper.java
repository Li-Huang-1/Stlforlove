package org.example.myblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.myblog.domain.entity.User;

@Mapper
public interface UserMapper {

    User queryByUsername(@Param("username") String username);

    // 更新用户的 Token
    void updateToken(User user);

    // 根据 Token 查询用户
    User queryByToken(@Param("token") String token);
}
