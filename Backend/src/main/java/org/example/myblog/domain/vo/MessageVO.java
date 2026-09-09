package org.example.myblog.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * 留言展示对象
 * 对外返回时使用，屏蔽实体中的 updateTime 等内部字段
 */
@Data
public class MessageVO {
    /** 留言ID */
    private Long id;
    /** 昵称 */
    private String nickname;
    /** 留言内容 */
    private String content;
    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private LocalDateTime createTime;
}
