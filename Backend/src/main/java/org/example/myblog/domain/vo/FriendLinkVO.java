package org.example.myblog.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 友链展示对象
 * 对外返回时使用，屏蔽实体中的 updateTime 等内部字段
 */
@Data
public class FriendLinkVO {
    /** 友链ID */
    private Long id;
    /** 站点名称 */
    private String name;
    /** 站点链接 */
    private String url;
    /** 站点图标 */
    private String logo;
    /** 站点描述 */
    private String description;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
