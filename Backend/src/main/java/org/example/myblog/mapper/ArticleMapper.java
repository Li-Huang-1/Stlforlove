package org.example.myblog.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.example.myblog.domain.dto.ArticleQueryDTO;
import org.example.myblog.domain.entity.Article;
import org.example.myblog.domain.vo.ArticleListDetailVO;
import org.example.myblog.domain.vo.ArticleListVO;

@Mapper
public interface ArticleMapper extends BaseMapper<Article> {

    /**
     * 文章详情列表(含 content),支持按 includeDraft 过滤草稿
     */
    IPage<ArticleListDetailVO> queryArticleListDetail(IPage<ArticleListDetailVO> page, @Param("query") ArticleQueryDTO query);

    /**
     * 文章简略列表(不含 content)
     */
    IPage<ArticleListVO> queryArticleList(IPage<ArticleListVO> page, @Param("query") ArticleQueryDTO query);
}
