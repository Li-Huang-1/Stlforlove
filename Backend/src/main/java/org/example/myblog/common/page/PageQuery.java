package org.example.myblog.common.page;

import lombok.Data;

/**
 * 通用分页参数
 * 业务相关的查询字段请放到具体的 DTO(如 ArticleQueryDTO)中继承本类
 */
@Data
public class PageQuery {

    private Integer pageNum;

    private Integer pageSize;

}
