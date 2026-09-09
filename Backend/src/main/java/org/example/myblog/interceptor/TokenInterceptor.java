package org.example.myblog.interceptor;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.example.myblog.domain.entity.User;
import org.example.myblog.mapper.UserMapper;
import org.example.myblog.utils.UserContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class TokenInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 跨域
        if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
            response.setStatus(200);
            return true;
        }

        // 1. 从请求头取出 Token
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Bearer ")) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"未登录，请先登录\"}");
            return false;
        }

        // 从第7个字符开始截取token
        String token = authorization.substring(7);

        // 2. 构建 Token 查询条件并查询用户
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<User>()
                .select(User::getId, User::getUsername, User::getToken, User::getAvatar)
                .eq(User::getToken, token);

        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            response.setStatus(401);
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":401,\"msg\":\"Token 无效，请重新登录\"}");
            return false;
        }

        // 3. 把用户信息存到 ThreadLocal 里，方便后续接口使用
        UserContext.setCurrentUser(user);

        return true;
    }

    /**
     * 请求完成后清理 ThreadLocal，防止内存泄漏
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        UserContext.clear();
    }
}