package com.mattlalonde.website.admin.articles;

import com.mattlalonde.website.admin.articles.domain.Article;
import com.mattlalonde.website.admin.articles.domain.commands.*;
import com.mattlalonde.website.admin.common.exceptions.EntityNotFoundException;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@RestController
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @Getter
    @Setter
    public static class CreateArticleRequest {
        @NotEmpty private String title;
    }

    @PostMapping("/api/article/create")
    public Article createArticle(@RequestBody @Valid CreateArticleRequest request) {
        return articleService.createArticle(new CreateArticleCommand(UUID.randomUUID(), request.getTitle(), LocalDateTime.now()));
    }

    @Getter
    @Setter
    public static class UpdateArticleRequest {
        @NotEmpty private String title;
        private String precis;
        private String body;
    }

    @PutMapping("/api/article/{id}/update")
    public Article updateArticle(@PathVariable("id") String id, @RequestBody @Valid UpdateArticleRequest request) {
        return articleService.updateArticle(new UpdateArticleCommand(UUID.fromString(id), request.title, request.precis, request.body));
    }

    @Getter
    @Setter
    public static class PublishArticleRequest {
        @NotNull private LocalDateTime publicationDate;
    }

    @PutMapping("/api/article/{id}/publish")
    public Article publishArticle(@PathVariable("id") String id, @RequestBody @Valid PublishArticleRequest request) {
        return articleService.publishArticle(new PublishArticleCommand(UUID.fromString(id), request.getPublicationDate()));
    }

    @PutMapping("/api/article/{id}/takeoffline")
    public Article unpublishArticle(@PathVariable("id") String id) {
        return articleService.takeArticleOffline(new UnpublishArticleCommand(UUID.fromString(id)));
    }

    @DeleteMapping("/api/article/{id}")
    public Article deleteArticle(@PathVariable("id") String id) {
        return articleService.deleteArticle(new DeleteArticleCommand(UUID.fromString(id)));
    }

    @PutMapping("/api/article/{id}/reinstate")
    public Article reinstateArticle(@PathVariable("id") String id) {
        return articleService.reinstateArticle(new ReinstateArticleCommand(UUID.fromString(id)));
    }

    @Getter
    @Setter
    public static class ArticleTagRequest {
        @NotNull private String tagId;
    }

    @PutMapping("/api/article/{id}/addtag")
    public Article addTagToArticle(@PathVariable("id") String id, @RequestBody @Valid ArticleTagRequest request) {
        return articleService.addTagToArticle(new AddArticleTagCommand(UUID.fromString(id), UUID.fromString(request.getTagId())));
    }

    @PutMapping("/api/article/{id}/removetag")
    public Article removeTagFromArticle(@PathVariable("id") String id, @RequestBody @Valid ArticleTagRequest request) {
        return articleService.removeTagFromArticle(new RemoveArticleTagCommand(UUID.fromString(id), UUID.fromString(request.getTagId())));
    }

    @GetMapping("/api/article/all")
    public List<Article> getAllArticles() {
        return articleService.getAll();
    }

    @GetMapping("/api/article/{id}")
    public Article getById(@PathVariable("id") String id) throws EntityNotFoundException {
        return articleService.getById(UUID.fromString(id));
    }
}
