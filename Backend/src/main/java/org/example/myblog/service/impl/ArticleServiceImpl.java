package org.example.myblog.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.example.myblog.domain.dto.ArticleQueryDTO;
import org.example.myblog.domain.entity.Article;
import org.example.myblog.domain.vo.ArticleListDetailVO;
import org.example.myblog.domain.vo.ArticleListVO;
import org.example.myblog.mapper.ArticleMapper;
import org.example.myblog.service.IArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements IArticleService {

    @Autowired
    private ArticleMapper mapper;

    @Override
    public int saveArticle(Article article) {
        // 返回受影响行数
        return mapper.insert(article);
    }

    @Override
    public int deleteArticleById(int id) {
        return mapper.deleteById(id);
    }

    @Override
    public int updateArticleById(Article article) {
        return mapper.updateById(article);
    }

    @Override
    public IPage<ArticleListDetailVO> queryArticleListDetail(ArticleQueryDTO query) {
        Page<ArticleListDetailVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return mapper.queryArticleListDetail(page, query);
    }

    @Override
    public IPage<ArticleListVO> queryArticleList(ArticleQueryDTO query) {
        Page<ArticleListVO> page = new Page<>(query.getPageNum(), query.getPageSize());
        return mapper.queryArticleList(page, query);
    }

    @Override
    public ArticleListDetailVO queryArticleDetailById(int id) {
        Article article = mapper.selectById(id);

        if (article == null) {
            return null;
        }

        ArticleListDetailVO vo = new ArticleListDetailVO();
        BeanUtils.copyProperties(article, vo);
        return vo;
    }

    @Override
    public int countArticles() {
        // 构建查询条件:已发布文章(status=1)
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<Article>()
                .eq(Article::getStatus, 1);

        // 查询总数并转换为 int
        return Math.toIntExact(mapper.selectCount(queryWrapper));
    }
}
