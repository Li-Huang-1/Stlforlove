package org.example.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.myblog.domain.entity.Message;

/**
 * 留言 Mapper
 */
@Mapper
public interface MessageMapper extends BaseMapper<Message> {
}
