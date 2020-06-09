package com.mattlalonde.website.admin.articles;

import com.mattlalonde.website.admin.articles.domain.Article;
import com.mattlalonde.website.admin.articles.domain.ArticlePublicationChecks;
import com.mattlalonde.website.admin.articles.domain.commands.*;
import com.mattlalonde.website.admin.common.exceptions.EntityNotFoundException;
import org.springframework.data.domain.Sort;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Validator;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@Service
public class ArticleServiceImpl implements ArticleService {

    private final ArticleRepository articleRepository;

    private final Validator validator;

    public ArticleServiceImpl(ArticleRepository articleRepository, Validator validator) {
        this.articleRepository = articleRepository;
        this.validator = validator;
    }

    @Override
    public Article createArticle(CreateArticleCommand command) {
        Article newArticle = Article.create(command);
        return articleRepository.save(newArticle);
    }

    @Override
    public Article updateArticle(@NonNull UpdateArticleCommand command) {
        Article article = articleRepository.findById(command.getId())
                                           .orElseThrow(() -> new EntityNotFoundException(Article.class, "id", command.getId().toString()));
        article.apply(command);
        return articleRepository.save(article);
    }

    @Override
    public Article publishArticle(@NonNull PublishArticleCommand command) {
        Article article = articleRepository.findById(command.getId())
                                           .orElseThrow(() -> new EntityNotFoundException(Article.class, "id", command.getId().toString()));


        Set<ConstraintViolation<Article>> violations = validator.validate(article, ArticlePublicationChecks.class);

        if(violations.isEmpty()) {
            article.apply(command);
            return articleRepository.save(article);
        }

        throw new ConstraintViolationException(violations);
    }

    @Override
    public Article takeArticleOffline(UnpublishArticleCommand command) {
        Article article = articleRepository.findById(command.getId())
                                           .orElseThrow(() -> new EntityNotFoundException(Article.class, "id", command.getId().toString()));
        article.apply(command);
        return articleRepository.save(article);
    }

    @Override
    public Article deleteArticle(DeleteArticleCommand command) {
        Article article = articleRepository.findById(command.getId())
                                           .orElseThrow(() -> new EntityNotFoundException(Article.class, "id", command.getId().toString()));
        article.apply(command);
        return articleRepository.save(article);
    }

    @Override
    public Article reinstateArticle(ReinstateArticleCommand command) {
        Article article = articleRepository.findById(command.getId())
                                           .orElseThrow(() -> new EntityNotFoundException(Article.class, "id", command.getId().toString()));
        article.apply(command);
        return articleRepository.save(article);
    }

    @Override
    public Article addTagToArticle(AddArticleTagCommand command) {
        Article article = articleRepository.findById(command.getArticleId())
                .orElseThrow(() -> new EntityNotFoundException(Article.class, "id", command.getArticleId().toString()));

        article.apply(command);
        return articleRepository.save(article);
    }

    @Override
    public Article removeTagFromArticle(RemoveArticleTagCommand command) {
        Article article = articleRepository.findById(command.getArticleId())
                .orElseThrow(() -> new EntityNotFoundException(Article.class, "id", command.getArticleId().toString()));

        article.apply(command);
        return articleRepository.save(article);
    }

    @Override
    public List<Article> getAll() {
        return articleRepository.findAll(Sort.by(Sort.Direction.DESC,"createdTimestamp"));
    }

    @Override
    public Article getById(UUID id) {
        return articleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(Article.class, "id", id.toString()));
    }
}
