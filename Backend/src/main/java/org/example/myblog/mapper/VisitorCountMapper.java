package org.example.myblog.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.example.myblog.domain.entity.VisitorCount;

@Mapper
public interface VisitorCountMapper {

    VisitorCount getCount();

    int incrementCount();

    int tableExists();

    int initCount();
}
