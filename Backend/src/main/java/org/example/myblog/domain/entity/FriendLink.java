package org.example.myblog.domain.entity;

import com.baomidou.mybatisplus.annotation.FieldFill;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 友链实体
 * 对应数据库表 friend_links
 */
@Data
@TableName("friend_links")
public class FriendLink {

    /** 友链ID */
    @TableId(type = IdType.AUTO)
    private Long id;

    /** 站点名称 */
    private String name;

    /** 站点链接 */
    private String url;

    /** 站点图标（可空，为空时前端用名称首字符生成渐变头像） */
    private String logo;

    /** 站点描述（可空） */
    private String description;

    /** 创建时间（插入时由 FieldHandler 自动填充） */
    @TableField(fill = FieldFill.INSERT)
    private LocalDateTime createTime;

    /** 更新时间（插入/更新时由 FieldHandler 自动填充） */
    @TableField(fill = FieldFill.INSERT_UPDATE)
    private LocalDateTime updateTime;
}
