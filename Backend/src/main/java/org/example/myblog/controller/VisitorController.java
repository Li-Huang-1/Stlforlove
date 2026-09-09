package org.example.myblog.controller;

import org.example.myblog.common.domain.AjaxResult;
import org.example.myblog.domain.vo.VisitorCountVO;
import org.example.myblog.service.IVisitorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/public/visitor")
public class VisitorController {

    @Autowired
    private IVisitorService visitorService;

    /**
     * 增加访客计数（前端通过 sessionStorage 控制只计一次）
     */
    @GetMapping("/increment")
    public AjaxResult<VisitorCountVO> incrementCount() {
        VisitorCountVO vo = visitorService.incrementAndGetCount();
        return AjaxResult.success(vo);
    }

    /**
     * 获取当前访客总数
     */
    @GetMapping("/count")
    public AjaxResult<VisitorCountVO> getCount() {
        VisitorCountVO vo = visitorService.getCount();
        return AjaxResult.success(vo);
    }
}
