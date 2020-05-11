package com.mattlalonde.website.admin.articles;

import com.mattlalonde.website.admin.articles.domain.Article;
import com.mattlalonde.website.admin.articles.domain.commands.*;

import java.util.List;
import java.util.UUID;

public interface ArticleService {
    Article createArticle(CreateArticleCommand command);
    Article updateArticle(UpdateArticleCommand command);
    Article publishArticle(PublishArticleCommand command);
    Article takeArticleOffline(UnpublishArticleCommand command);
    Article deleteArticle(DeleteArticleCommand command);
    Article reinstateArticle(ReinstateArticleCommand command);

    Article addTagToArticle(AddArticleTagCommand command);
    Article removeTagFromArticle(RemoveArticleTagCommand command);

    List<Article> getAll();
    Article getById(UUID id);
}
