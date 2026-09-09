package org.example.myblog.controller;

import org.example.myblog.common.controller.BaseController;
import org.example.myblog.common.domain.AjaxResult;
import org.example.myblog.domain.dto.MessageSaveDTO;
import org.example.myblog.domain.entity.Message;
import org.example.myblog.service.IMessageService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 留言墙 Controller
 * - GET  /public/message/list：查询留言列表（公开）
 * - POST /public/message：发布留言（公开）
 */
@RestController
public class MessageController extends BaseController {

    @Autowired
    private IMessageService messageService;

    /**
     * 查询留言列表（按创建时间倒序）
     */
    @GetMapping("/public/message/list")
    public AjaxResult list() {
        return AjaxResult.success(messageService.queryMessageList());
    }

    /**
     * 发布留言
     * @Valid 触发 MessageSaveDTO 上的 JSR303 校验（@NotBlank/@Size），
     * 校验失败抛出 MethodArgumentNotValidException，由 GlobalExceptionHandler
     * 统一捕获并返回第一个错误消息，无需在方法体内手动校验
     */
    @PostMapping("/public/message")
    public AjaxResult save(@Valid @RequestBody MessageSaveDTO dto) {
        // DTO -> Entity
        Message message = new Message();
        BeanUtils.copyProperties(dto, message);

        return AjaxResult.success(messageService.saveMessage(message));
    }
}
