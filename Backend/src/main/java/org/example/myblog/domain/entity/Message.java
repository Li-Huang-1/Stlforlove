package org.example.myblog.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 留言墙实体
 * 对应数据库表 messages
 */
@Data
@TableName("messages")
public class Message {

    /** 留言ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 昵称 */
    private String nickname;

    /** 留言内容 */
    private String content;

    /** 创建时间（插入时由 FieldHandler 自动填充） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间（插入/更新时由 FieldHandler 自动填充） */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
