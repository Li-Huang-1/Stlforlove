package org.example.myblog.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myblog.domain.entity.VisitorCount;
import org.example.myblog.domain.vo.VisitorCountVO;
import org.example.myblog.mapper.VisitorCountMapper;
import org.example.myblog.service.IVisitorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl extends ServiceImpl<VisitorCountMapper, VisitorCount> implements IVisitorService {

    /** 访客记录固定使用 id=1 的那一行(单例记录) */
    private static final Long SINGLETON_ID = 1L;

    @Autowired
    private VisitorCountMapper mapper;

    @Override
    public VisitorCountVO incrementAndGetCount() {
        VisitorCount entity = mapper.selectById(SINGLETON_ID);

        // 首次访问:初始化
        if (entity == null) {
            entity = new VisitorCount();
            entity.setId(SINGLETON_ID);
            entity.setCount(1);
            mapper.insert(entity);
            return convertToVO(entity);
        }

        // 自增计数
        entity.setCount(entity.getCount() + 1);
        mapper.updateById(entity);

        return convertToVO(entity);
    }

    @Override
    public VisitorCountVO getCount() {
        return convertToVO(mapper.selectById(SINGLETON_ID));
    }

    /**
     * Entity 转 VO
     */
    private VisitorCountVO convertToVO(VisitorCount entity) {
        if (entity == null) {
            return null;
        }
        VisitorCountVO vo = new VisitorCountVO();
        BeanUtils.copyProperties(entity, vo);
        return vo;
    }
}
