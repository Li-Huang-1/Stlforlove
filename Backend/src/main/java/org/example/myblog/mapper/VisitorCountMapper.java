package org.example.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.example.myblog.domain.entity.VisitorCount;

@Mapper
public interface VisitorCountMapper extends BaseMapper<VisitorCount> {
}
