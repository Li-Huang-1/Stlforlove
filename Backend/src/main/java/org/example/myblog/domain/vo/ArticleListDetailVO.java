package org.example.myblog.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ArticleListDetailVO {
    private Integer id;
    private String title;
    private String type;
    private String content;
    private LocalDateTime createTime;
    private Integer status;
    private String category;
    private String coverUrl;
}
