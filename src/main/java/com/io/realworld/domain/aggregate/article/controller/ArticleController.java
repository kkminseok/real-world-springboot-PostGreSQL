package com.io.realworld.domain.aggregate.article.controller;

import com.io.realworld.domain.aggregate.article.dto.ArticleParam;
import com.io.realworld.domain.aggregate.article.dto.ArticleUpdate;
import com.io.realworld.domain.aggregate.article.dto.Articledto;
import com.io.realworld.domain.aggregate.article.dto.ArticleResponse;
import com.io.realworld.domain.aggregate.article.service.ArticleService;
import com.io.realworld.domain.aggregate.user.dto.UserAuth;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping()
    public List<ArticleResponse> getArticles(@AuthenticationPrincipal UserAuth userAuth,@ModelAttribute ArticleParam articleParam){
        return articleService.getArticles(articleParam);
    }

    @GetMapping("/{slug}")
    public ArticleResponse getArticle(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug) {
        return articleService.getArticle(userAuth, slug);
    }

    @PostMapping
    public ArticleResponse createArticle(@AuthenticationPrincipal UserAuth userAuth, @Valid @RequestBody Articledto articledto) {
        return articleService.createArticle(userAuth, articledto);
    }

    @PutMapping("/{slug}")
    public ArticleResponse updateArticle(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug, @Valid @RequestBody ArticleUpdate articleUpdate) {
        return articleService.updateArticle(userAuth, slug, articleUpdate);
    }

    @DeleteMapping("/{slug}")
    public void deleteArticle(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug) {
        articleService.deleteArticle(userAuth, slug);
    }

    @PostMapping("/{slug}/favorite")
    public ArticleResponse favoriteArticle(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug){
        return articleService.favoriteArticle(userAuth, slug);
    }

    @DeleteMapping("/{slug}/favorite")
    public ArticleResponse unFavoriteArticle(@AuthenticationPrincipal UserAuth userAuth, @PathVariable("slug") String slug){
        return articleService.unFavoriteArticle(userAuth, slug);
    }
}
