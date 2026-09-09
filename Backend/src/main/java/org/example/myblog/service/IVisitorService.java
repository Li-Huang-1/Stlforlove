package org.example.myblog.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.example.myblog.domain.entity.VisitorCount;
import org.example.myblog.domain.vo.VisitorCountVO;

public interface IVisitorService extends IService<VisitorCount> {

    VisitorCountVO incrementAndGetCount();

    VisitorCountVO getCount();
}
