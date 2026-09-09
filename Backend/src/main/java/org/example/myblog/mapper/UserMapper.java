package org.example.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.myblog.domain.entity.User;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
