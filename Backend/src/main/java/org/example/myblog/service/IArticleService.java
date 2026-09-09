package org.example.myblog.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import org.example.myblog.domain.dto.ArticleQueryDTO;
import org.example.myblog.domain.entity.Article;
import org.example.myblog.domain.vo.ArticleListDetailVO;
import org.example.myblog.domain.vo.ArticleListVO;

public interface IArticleService extends IService<Article> {

    /**
     * 保存文章(返回影响行数)
     */
    int saveArticle(Article article);

    /**
     * 通过id删除文章
     */
    int deleteArticleById(int id);

    /**
     * 通过id更新文章
     */
    int updateArticleById(Article article);

    /**
     * 查询文章详情列表(含 content)
     */
    IPage<ArticleListDetailVO> queryArticleListDetail(ArticleQueryDTO query);

    /**
     * 查询文章简略列表(不含 content)
     */
    IPage<ArticleListVO> queryArticleList(ArticleQueryDTO query);

    /**
     * 通过id查询文章详情
     */
    ArticleListDetailVO queryArticleDetailById(int id);

    /**
     * 查询已发布文章总数
     */
    int countArticles();
}
