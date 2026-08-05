package org.example.myblog.service.impl;

import org.example.myblog.domain.entity.VisitorCount;
import org.example.myblog.domain.vo.VisitorCountVO;
import org.example.myblog.mapper.VisitorCountMapper;
import org.example.myblog.service.VisitorService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class VisitorServiceImpl implements VisitorService {

    @Autowired
    private VisitorCountMapper visitorCountMapper;

    @Override
    public VisitorCountVO incrementAndGetCount() {
        // 检查表是否存在记录
        if (visitorCountMapper.tableExists() == 0) {
            // 初始化表格
            visitorCountMapper.initCount();
            return convertToVO(visitorCountMapper.getCount());
        }

        // 增加计数
        visitorCountMapper.incrementCount();

        // 获取更新后的计数并转换为 VO
        return convertToVO(visitorCountMapper.getCount());
    }

    @Override
    public VisitorCountVO getCount() {
        return convertToVO(visitorCountMapper.getCount());
    }

    /**
     * Entity 转 VO
     * @return VO 对象，如果 entity 为 null 则返回 null
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
