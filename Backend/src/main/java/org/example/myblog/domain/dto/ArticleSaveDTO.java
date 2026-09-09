package org.example.myblog.domain.dto;

import lombok.Data;

@Data
public class ArticleSaveDTO {
    private String title;
    private String content;
    private String type;
    private Integer status;
    private String category;
    private String coverUrl;
}
