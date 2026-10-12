package org.example.myblog.domain.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

/**
 * 友链保存参数
 * 仅接收名称、链接、图标、描述，避免客户端直接传入 id/createTime 造成篡改
 */
@Data
public class FriendLinkSaveDTO {

    /** 站点名称 */
    @NotBlank(message = "站点名称不能为空")
    @Size(max = 50, message = "站点名称长度不能超过 50 个字符")
    private String name;

    /** 站点链接（必须携带 http/https 协议头，否则浏览器会当相对路径解析） */
    @NotBlank(message = "站点链接不能为空")
    @Pattern(regexp = "^https?://\\S+$", message = "站点链接需以 http:// 或 https:// 开头")
    @Size(max = 255, message = "站点链接长度不能超过 255 个字符")
    private String url;

    /** 站点图标（可空） */
    @Size(max = 255, message = "站点图标长度不能超过 255 个字符")
    private String logo;

    /** 站点描述（可空） */
    @Size(max = 100, message = "站点描述长度不能超过 100 个字符")
    private String description;
}
