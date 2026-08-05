package org.example.myblog.common.page;

import lombok.Data;

@Data
public class PageQuery {

    private Integer pageNum;

    private Integer pageSize;

    /**
     * 是否包含草稿文章（status=0）
     * true - 包含所有文章
     * false/null - 只显示已发布文章（status=1）
     */
    private Boolean includeDraft;

}
