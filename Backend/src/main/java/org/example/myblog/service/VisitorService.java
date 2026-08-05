package org.example.myblog.service;

import org.example.myblog.domain.vo.VisitorCountVO;

public interface VisitorService {

    VisitorCountVO incrementAndGetCount();

    VisitorCountVO getCount();
}
