package org.example.myblog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import org.example.myblog.common.controller.BaseController;
import org.example.myblog.common.domain.AjaxResult;
import org.example.myblog.common.domain.TableDataInfo;
import org.example.myblog.domain.dto.ArticleQueryDTO;
import org.example.myblog.domain.dto.ArticleSaveDTO;
import org.example.myblog.domain.dto.ArticleUpdateDTO;
import org.example.myblog.domain.entity.Article;
import org.example.myblog.domain.vo.ArticleListDetailVO;
import org.example.myblog.domain.vo.ArticleListVO;
import org.example.myblog.service.IArticleService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class ArticleController extends BaseController {

    @Autowired
    private IArticleService articleService;

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
    public TableDataInfo listdetail(@RequestBody ArticleQueryDTO query) {

        // 调用Service层分页查询
        IPage<ArticleListDetailVO> page = articleService.queryArticleListDetail(query);

        // 返回分页数据
        return getDataInfo(page.getRecords(), page.getTotal());
    }

    // 简略接口 (无content)
    @PostMapping("/public/article/list")
    public TableDataInfo list(@RequestBody ArticleQueryDTO query) {

        // 调用Service层分页查询
        IPage<ArticleListVO> page = articleService.queryArticleList(query);

        // 返回分页数据
        return getDataInfo(page.getRecords(), page.getTotal());
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