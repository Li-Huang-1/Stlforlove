package org.example.myblog.controller;

import org.example.myblog.common.controller.BaseController;
import org.example.myblog.common.domain.AjaxResult;
import org.example.myblog.domain.dto.FriendLinkSaveDTO;
import org.example.myblog.domain.entity.FriendLink;
import org.example.myblog.service.IFriendLinkService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * 友链 Controller
 * - GET    /public/friend-link/list：查询友链列表（公开）
 * - POST   /admin/friend-link：新增友链（需登录）
 * - DELETE /admin/friend-link/{id}：删除友链（需登录）
 */
@RestController
public class FriendLinkController extends BaseController {

    @Autowired
    private IFriendLinkService friendLinkService;

    /**
     * 查询友链列表（按创建时间正序）
     */
    @GetMapping("/public/friend-link/list")
    public AjaxResult list() {
        return AjaxResult.success(friendLinkService.queryFriendLinkList());
    }

    /**
     * 新增友链
     * @Valid 触发 FriendLinkSaveDTO 上的 JSR303 校验（@NotBlank/@Size），
     * 校验失败抛出 MethodArgumentNotValidException，由 GlobalExceptionHandler
     * 统一捕获并返回第一个错误消息，无需在方法体内手动校验
     */
    @PostMapping("/admin/friend-link")
    public AjaxResult save(@Valid @RequestBody FriendLinkSaveDTO dto) {
        // DTO -> Entity
        FriendLink friendLink = new FriendLink();
        BeanUtils.copyProperties(dto, friendLink);

        return AjaxResult.success(friendLinkService.saveFriendLink(friendLink));
    }

    /**
     * 删除友链
     */
    @DeleteMapping("/admin/friend-link/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        friendLinkService.removeFriendLink(id);
        return AjaxResult.success();
    }
}
