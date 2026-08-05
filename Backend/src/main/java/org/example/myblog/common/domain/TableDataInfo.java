package org.example.myblog.common.domain;

import lombok.Data;

/*
    列表数据响应实体
 */
@Data
public class TableDataInfo<T> {

    /*
        状态码
     */
    private Integer code;

    /*
        操作消息
     */
    private String msg;

    /*
        列表数据
     */
    private T rows;

    /*
        总条数
     */
    private Long total;

}
