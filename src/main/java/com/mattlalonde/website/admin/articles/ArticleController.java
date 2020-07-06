package com.mattlalonde.website.admin.articles;

import com.mattlalonde.website.admin.articles.domain.Article;
import com.mattlalonde.website.admin.articles.domain.commands.*;
import com.mattlalonde.website.admin.articles.dtos.ArticleDTO;
import com.mattlalonde.website.admin.articles.dtos.ArticleListDTO;
import com.mattlalonde.website.admin.common.exceptions.EntityNotFoundException;
import com.mattlalonde.website.admin.tags.TagService;
import com.mattlalonde.website.admin.tags.domain.Tag;
import lombok.Getter;
import lombok.Setter;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@Secured("ROLE_ADMIN")
public class ArticleController {

    private final ArticleService articleService;
    private final TagService tagService;

    public ArticleController(ArticleService articleService, TagService tagService) {
        this.articleService = articleService;
        this.tagService = tagService;
    }

    @Getter
    @Setter
    public static class CreateArticleRequest {
        @NotEmpty private String title;
    }

    @PostMapping("/api/article/create")
    public ArticleDTO createArticle(@RequestBody @Valid CreateArticleRequest request) {
        Article article = articleService.createArticle(new CreateArticleCommand(UUID.randomUUID(), request.getTitle(), LocalDateTime.now()));
        return ArticleDtoBuilder.buildDetails(article, new ArrayList<>());
    }

    @Getter
    @Setter
    public static class UpdateArticleRequest {
        @NotEmpty private String title;
        private String precis;
        private String body;
    }

    @PutMapping("/api/article/{id}/update")
    public ArticleDTO updateArticle(@PathVariable("id") String id, @RequestBody @Valid UpdateArticleRequest request) {
        Article article =  articleService.updateArticle(new UpdateArticleCommand(UUID.fromString(id), request.title, request.precis, request.body));

        return getArticleDto(article);
    }

    @Getter
    @Setter
    public static class PublishArticleRequest {
        @NotNull private LocalDateTime publicationDate;
    }

    @PutMapping("/api/article/{id}/publish")
    public ArticleDTO publishArticle(@PathVariable("id") String id, @RequestBody @Valid PublishArticleRequest request) {
        Article article =  articleService.publishArticle(new PublishArticleCommand(UUID.fromString(id), request.getPublicationDate()));

        return getArticleDto(article);
    }

    @PutMapping("/api/article/{id}/takeoffline")
    public ArticleDTO unpublishArticle(@PathVariable("id") String id) {
        Article article = articleService.takeArticleOffline(new UnpublishArticleCommand(UUID.fromString(id)));

        return getArticleDto(article);
    }

    @DeleteMapping("/api/article/{id}")
    public ArticleDTO deleteArticle(@PathVariable("id") String id) {
        Article article = articleService.deleteArticle(new DeleteArticleCommand(UUID.fromString(id)));

        return getArticleDto(article);
    }

    @PutMapping("/api/article/{id}/reinstate")
    public ArticleDTO reinstateArticle(@PathVariable("id") String id) {
        Article article = articleService.reinstateArticle(new ReinstateArticleCommand(UUID.fromString(id)));

        return getArticleDto(article);
    }

    @Getter
    @Setter
    public static class ArticleTagRequest {
        @NotNull private String tagId;
    }

    @PutMapping("/api/article/{id}/addtag")
    public ArticleDTO addTagToArticle(@PathVariable("id") String id, @RequestBody @Valid ArticleTagRequest request) {
        Article article = articleService.addTagToArticle(new AddArticleTagCommand(UUID.fromString(id), UUID.fromString(request.getTagId())));

        return getArticleDto(article);
    }

    @PutMapping("/api/article/{id}/removetag")
    public ArticleDTO removeTagFromArticle(@PathVariable("id") String id, @RequestBody @Valid ArticleTagRequest request) {
        Article article = articleService.removeTagFromArticle(new RemoveArticleTagCommand(UUID.fromString(id), UUID.fromString(request.getTagId())));

        return getArticleDto(article);
    }

    @GetMapping("/api/article/all")
    public ArticleListDTO getAllArticles() {

        List<Article> articles = articleService.getAll();
        List<UUID> tagIds = articles.stream()
                                    .flatMap(article -> article.getTags().stream().map(tag -> tag.getTagId()))
                                    .distinct()
                                    .collect(Collectors.toList());
        List<Tag> tags = tagService.get(tagIds);

        return ArticleDtoBuilder.buildList(articles, tags);
    }

    @GetMapping("/api/article/{id}")
    public ArticleDTO getById(@PathVariable("id") String id) throws EntityNotFoundException {
        return getArticleDto(UUID.fromString(id));
    }

    private ArticleDTO getArticleDto(UUID id) {
        Article article = articleService.getById(id);
        return getArticleDto(article);
    }

    private ArticleDTO getArticleDto(Article article) {
        List<Tag> tags  = tagService.get(article.getTags().stream().map(tag -> tag.getTagId()).collect(Collectors.toList()));

        return ArticleDtoBuilder.buildDetails(article, tags);
    }
}
