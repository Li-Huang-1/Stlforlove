package org.example.myblog.domain.dto;

import lombok.Data;

@Data
public class ArticleUpdateDTO {
    private Integer id;
    private String title;
    private String content;
    private Integer status;
    private String category;
    private String coverUrl;
}
