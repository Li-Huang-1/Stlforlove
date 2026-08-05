package org.example.myblog.mapper;

import org.example.myblog.common.page.PageQuery;
import org.example.myblog.domain.entity.Article;
import org.apache.ibatis.annotations.Mapper;
import org.example.myblog.domain.vo.ArticleListDetailVO;
import org.example.myblog.domain.vo.ArticleListVO;

import java.util.List;

@Mapper
public interface ArticleMapper {

    int insertArticle(Article article);

    int deleteById(int id);

    int updateArticleById(Article article);

    List<ArticleListDetailVO> queryArticleListDetail(PageQuery pageQuery);

    List<ArticleListVO> queryArticleList(PageQuery pageQuery);

    Article queryArticleDetailById(int id);

    int countArticles();
}