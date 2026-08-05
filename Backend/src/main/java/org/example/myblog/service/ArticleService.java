package org.example.myblog.service;

import com.github.pagehelper.PageInfo;
import org.example.myblog.common.page.PageQuery;
import org.example.myblog.domain.entity.Article;
import org.example.myblog.domain.vo.ArticleListDetailVO;
import org.example.myblog.domain.vo.ArticleListVO;

public interface ArticleService {

    /**
     * 保存文章
     * @param article
     * @return
     */
    int saveArticle(Article article);

    /**
     * 通过id删除文章
     * @param id
     * @return
     */
    int deleteArticleById(int id);

    /**
     * 通过id更新文章
     * @param article
     * @return
     */
    int updateArticleById(Article article);

    /**
     * 查询文章详情列表
     * @param page
     * @return
     */
    PageInfo<ArticleListDetailVO> queryArticleListDetail(PageQuery page);

    /**
     * 查询文章简略列表
     * @param page
     * @return
     */
    PageInfo<ArticleListVO> queryArticleList(PageQuery page);

    /**
     * 通过id查询文章详情
     * @param id
     * @return
     */
    ArticleListDetailVO queryArticleDetailById(int id);

    /**
     * 查询已发布文章总数
     * @return
     */
    int countArticles();
}
