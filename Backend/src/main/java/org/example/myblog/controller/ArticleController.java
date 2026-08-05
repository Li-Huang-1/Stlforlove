package org.example.myblog.controller;

import com.github.pagehelper.PageInfo;
import org.example.myblog.common.controller.BaseController;
import org.example.myblog.common.domain.AjaxResult;
import org.example.myblog.common.domain.TableDataInfo;
import org.example.myblog.common.page.PageQuery;
import org.example.myblog.domain.dto.ArticleSaveDTO;
import org.example.myblog.domain.dto.ArticleUpdateDTO;
import org.example.myblog.domain.entity.Article;
import org.example.myblog.domain.vo.ArticleListDetailVO;
import org.example.myblog.domain.vo.ArticleListVO;
import org.example.myblog.service.ArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController extends BaseController {

    @Autowired
    private ArticleService articleService;

    @PostMapping("/admin/article/new")
    public AjaxResult save(@RequestBody ArticleSaveDTO dto) {

        Article article = new Article();

        BeanUtils.copyProperties(dto, article);

        return toAjax(articleService.saveArticle(article));
    }

    @PostMapping("/admin/article/update")
    public AjaxResult update(@RequestBody ArticleUpdateDTO dto) {

        Article article = new Article();

        BeanUtils.copyProperties(dto, article);

        return toAjax(articleService.updateArticleById(article));
    }

    // 详情接口 (有content)
    @PostMapping("/public/article/listdetail")
    public TableDataInfo listdetail(@RequestBody PageQuery pageQuery) {

        // 调用Service层分页查询
        PageInfo<ArticleListDetailVO> pageInfo = articleService.queryArticleListDetail(pageQuery);

        // 返回分页数据
        return getDataInfo(pageInfo.getList(), pageInfo.getTotal());
    }

    // 简略接口 (无content)
    @PostMapping("/public/article/list")
    public TableDataInfo list(@RequestBody PageQuery pageQuery) {

        // 调用Service层分页查询
        PageInfo<ArticleListVO> pageInfo = articleService.queryArticleList(pageQuery);

        // 返回分页数据
        return getDataInfo(pageInfo.getList(), pageInfo.getTotal());
    }

    @GetMapping("/public/article/{id}")
    public AjaxResult getById(@PathVariable int id) {
        ArticleListDetailVO article = articleService.queryArticleDetailById(id);
        return AjaxResult.success(article);
    }

    @DeleteMapping("/admin/article/delete/{id}")
    public AjaxResult deleteById(@PathVariable int id) {

        return AjaxResult.success(articleService.deleteArticleById(id));
    }

    @GetMapping("/public/article/count")
    public AjaxResult countArticles() {
        int count = articleService.countArticles();
        return AjaxResult.success(count);
    }
}