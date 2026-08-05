package org.example.myblog.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.example.myblog.common.page.PageQuery;
import org.example.myblog.domain.entity.Article;
import org.example.myblog.domain.vo.ArticleListDetailVO;
import org.example.myblog.domain.vo.ArticleListVO;
import org.example.myblog.mapper.ArticleMapper;
import org.example.myblog.service.ArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ArticleServiceImpl implements ArticleService {

    @Autowired
    private ArticleMapper articleMapper;

    @Override
    public int saveArticle(Article article) {
        return articleMapper.insertArticle(article);
    }

    @Override
    public int deleteArticleById(int id) {
        return articleMapper.deleteById(id);
    }

    @Override
    public int updateArticleById(Article article) {
        return articleMapper.updateArticleById(article);
    }

    @Override
    public PageInfo<ArticleListDetailVO> queryArticleListDetail(PageQuery page) {

        // 使用 PageHelper 设置分页参数（必须在查询前）
        PageHelper.startPage(page.getPageNum(), page.getPageSize());

        // 执行查询（PageHelper 会自动拦截并加上 LIMIT）
        List<ArticleListDetailVO> articleList = articleMapper.queryArticleListDetail(page);

        // 用 PageInfo 包装结果（包含总记录数、总页数等）
        PageInfo<ArticleListDetailVO> pageInfo = new PageInfo<>(articleList);

        return pageInfo;
    }

    @Override
    public PageInfo<ArticleListVO> queryArticleList(PageQuery page) {

        PageHelper.startPage(page.getPageNum(), page.getPageSize());

        List<ArticleListVO> articleList = articleMapper.queryArticleList(page);

        PageInfo<ArticleListVO> pageInfo = new PageInfo<>(articleList);

        return pageInfo;
    }

    @Override
    public ArticleListDetailVO queryArticleDetailById(int id) {
        // Mapper 返回 Entity
        Article article = articleMapper.queryArticleDetailById(id);

        // 转换为 VO
        ArticleListDetailVO vo = new ArticleListDetailVO();

        BeanUtils.copyProperties(article, vo);

        return vo;
    }

    @Override
    public int countArticles() {
        return articleMapper.countArticles();
    }
}