package org.example.myblog.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 留言发布参数
 * 仅接收昵称与内容，避免客户端直接传入 id/createTime 造成篡改
 */
@Data
public class MessageSaveDTO {

    /** 昵称 */
    @NotBlank(message = "昵称不能为空")
    @Size(max = 20, message = "昵称长度不能超过 20 个字符")
    private String nickname;

    /** 留言内容 */
    @NotBlank(message = "留言内容不能为空")
    @Size(max = 200, message = "留言内容长度不能超过 200 个字符")
    private String content;
}
