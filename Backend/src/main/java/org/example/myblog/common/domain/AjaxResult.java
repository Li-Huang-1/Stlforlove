package org.example.myblog.common.domain;

import lombok.Data;

/*
    数据响应
 */
@Data
public class AjaxResult<T> {

    private Integer code;

    private String msg;

    private T data;

    public AjaxResult(Integer code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    /**
     * 成功响应示例
     * @return
     */
    public static AjaxResult success(){
        return new AjaxResult(200,"操作成功",null);
    }

    /**
     * 成功响应示例
     * @param data 数据对象
     * @return
     */
    public static AjaxResult success(Object data){
        return new AjaxResult(200,"操作成功",data);
    }

    /**
     * 成功响应示例
     * @param code 状态码
     * @param msg 操作消息
     * @param data 数据对象
     * @return
     */
    public static AjaxResult success(Integer code,String msg,Object data){
        return new AjaxResult(code,msg,data);
    }

    /**
     * 失败响应示例
     * @return
     */
    public static AjaxResult error(){
        return new AjaxResult(500,"操作失败",null);
    }

    /**
     * 失败响应示例
     * @param code 状态码
     * @param msg 操作消息
     * @param data 数据对象
     * @return
     */
    public static AjaxResult error(Integer code,String msg,Object data){
        return new AjaxResult(code,msg,data);
    }

}
