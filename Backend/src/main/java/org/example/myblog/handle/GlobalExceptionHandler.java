package org.example.myblog.handle;

import org.example.myblog.common.domain.AjaxResult;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

//todo 全局异常处理器
@RestControllerAdvice // 声明为全局异常处理器
public class GlobalExceptionHandler {
    /**
     * 处理 @RequestBody 参数验证异常
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public AjaxResult handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
    // 获取第一个错误信息
        String message = e.getBindingResult().getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }
    /**
     * 处理表单参数验证异常
     */
    @ExceptionHandler(BindException.class)
    public AjaxResult handleBindException(BindException e) {
        String message = e.getAllErrors().get(0).getDefaultMessage();
        return AjaxResult.error(message);
    }
    /**
     * 处理业务校验异常
     * ServiceImpl 层抛出的 IllegalArgumentException（如参数非法）由这里统一捕获，
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public AjaxResult handleIllegalArgumentException(IllegalArgumentException e) {
        return AjaxResult.error(e.getMessage());
    }

    /**
     * 处理其他异常
     */
    @ExceptionHandler(Exception.class)
    public AjaxResult handleException(Exception e) {
        return AjaxResult.error("服务器内部错误：" + e.getMessage());
    }
}