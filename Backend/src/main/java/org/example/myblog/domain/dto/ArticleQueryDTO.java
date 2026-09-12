package org.example.myblog.domain.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.example.myblog.common.page.PageQuery;

/**
 * 文章列表查询参数
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ArticleQueryDTO extends PageQuery {

    /**
     * 是否包含草稿文章(status=0)
     * true  - 包含所有文章
     * false/null - 只显示已发布文章(status=1)
     */
    private Boolean includeDraft;

    /**
     * 文章类型过滤: tech-技术 project-项目 log-日志
     */
    private String type;

    /** 标题关键词（模糊搜索） */
    private String keyword;

    /** 分类标签（精确匹配 category） */
    private String category;

}
