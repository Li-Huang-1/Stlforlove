package org.example.myblog.controller;

import org.example.myblog.common.controller.BaseController;
import org.example.myblog.common.domain.AjaxResult;
import org.example.myblog.domain.dto.LoginDTO;
import org.example.myblog.domain.entity.User;
import org.example.myblog.domain.vo.LoginVO;
import org.example.myblog.domain.vo.UserInfoVO;
import org.example.myblog.service.IUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public")
public class UserController extends BaseController {

    @Autowired
    private IUserService userService;

    /**
     * 登录接口
     */
    @PostMapping("/user/login")
    public AjaxResult login(@RequestBody LoginDTO dto) {
        // 调用 Service 层进行登录验证
        User user = userService.login(dto.getUsername(), dto.getPassword());

        // 登录失败
        if (user == null) {
            return AjaxResult.error(401, "用户名或密码错误", null);
        }

        // 登录成功，封装返回数据
        LoginVO loginVO = new LoginVO();

        // 将数据库查询到的用户信息（包含 avatar）复制到 LoginVO
        BeanUtils.copyProperties(user, loginVO);

        return AjaxResult.success(loginVO);
    }

    /**
     * 获取当前用户信息接口
     * 通过请求头中的 Authorization Token 查询用户信息
     */
    @GetMapping("/user/info")
    // required = false -> 该请求头不是必须的，如果不存在则注入 null（默认 true，缺失会报错）
    public AjaxResult getUserInfo(@RequestHeader(value = "Authorization", required = false) String authorization) {
        // 解析 Token（格式：Bearer xxx）
        String token = null;
        if (authorization != null && authorization.startsWith("Bearer ")) {
            token = authorization.substring(7);
        }

        // Token 为空
        if (token == null || token.isEmpty()) {
            return AjaxResult.error(401, "未登录", null);
        }

        // 根据 Token 查询用户信息
        User user = userService.getUserInfo(token);

        if (user == null) {
            return AjaxResult.error(401, "用户不存在或Token已过期", null);
        }

        // 封装用户信息返回（不包含密码）
        UserInfoVO userInfoVO = new UserInfoVO();

        BeanUtils.copyProperties(user, userInfoVO);

        return AjaxResult.success(userInfoVO);
    }
}