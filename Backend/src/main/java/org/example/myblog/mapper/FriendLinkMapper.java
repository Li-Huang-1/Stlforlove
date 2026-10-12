package org.example.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.myblog.domain.entity.FriendLink;

/**
 * 友链 Mapper
 */
@Mapper
public interface FriendLinkMapper extends BaseMapper<FriendLink> {
}
