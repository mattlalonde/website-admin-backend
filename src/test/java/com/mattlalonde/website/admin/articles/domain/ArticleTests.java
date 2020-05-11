package com.mattlalonde.website.admin.articles.domain;

import com.mattlalonde.website.admin.articles.domain.commands.CreateArticleCommand;
import com.mattlalonde.website.admin.articles.domain.commands.PublishArticleCommand;
import com.mattlalonde.website.admin.articles.domain.commands.UpdateArticleCommand;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

public class ArticleTests {

    @Test
    public void create_article_returns_initialised_article() {
        UUID id = UUID.randomUUID();
        String title = "title";
        LocalDateTime createdDate = LocalDateTime.now();

        CreateArticleCommand command = new CreateArticleCommand(id, title, createdDate);

        Article article = Article.create(command);

        assertThat(article.getId()).isEqualTo(id);
        assertThat(article.getTitle()).isEqualTo(title);
        assertThat(article.getCreatedTimestamp()).isEqualTo(createdDate);
        assertThat(article.getState()).isEqualTo(ArticleState.DRAFT);
        assertThat(article.getPrecis()).isNull();
        assertThat(article.getBody()).isNull();
        assertThat(article.getPublicationDate()).isNull();
    }

    @Test
    public void create_article_must_have_non_null_title() {
        UUID id = UUID.randomUUID();
        String title = null;
        LocalDateTime createdDate = LocalDateTime.now();

        assertThatNullPointerException().isThrownBy(() -> {
            CreateArticleCommand command = new CreateArticleCommand(id, title, createdDate);
        });
    }

    @Test
    public void create_article_must_have_non_empty_title() {
        UUID id = UUID.randomUUID();
        String title = " ";
        LocalDateTime createdDate = LocalDateTime.now();

        CreateArticleCommand command = new CreateArticleCommand(id, title, createdDate);

        assertThatIllegalArgumentException().isThrownBy(() -> {
            Article article = Article.create(command);
        });
    }

    @Test
    public void update_article_checks_ids_match() {
        UUID id = UUID.randomUUID();
        String title = "title";
        LocalDateTime createdDate = LocalDateTime.now();

        String precis = "precis";
        String body = "body";

        UUID otherId = UUID.randomUUID();

        Article testArticle = Article.create(new CreateArticleCommand(id, title, createdDate));

        assertThatIllegalArgumentException().isThrownBy(() -> {
            testArticle.apply(new UpdateArticleCommand(otherId, title, precis, body));
        });
    }

    @Test
    public void update_article_must_have_non_null_title() {
        UUID id = UUID.randomUUID();
        String title = "title";
        LocalDateTime createdDate = LocalDateTime.now();

        String newTitle = null;
        String precis = "precis";
        String body = "body";

        Article testArticle = Article.create(new CreateArticleCommand(id, title, createdDate));

        assertThatNullPointerException().isThrownBy(() -> {
            UpdateArticleCommand command = new UpdateArticleCommand(id, newTitle, precis, body);
        });
    }

    @Test
    public void update_article_must_have_non_empty_title() {
        UUID id = UUID.randomUUID();
        String title = "title";
        LocalDateTime createdDate = LocalDateTime.now();

        String newTitle = " ";
        String precis = "precis";
        String body = "body";

        Article testArticle = Article.create(new CreateArticleCommand(id, title, createdDate));

        assertThatIllegalArgumentException().isThrownBy(() -> {
            testArticle.apply(new UpdateArticleCommand(id, newTitle, precis, body));
        });
    }

    @Test
    public void update_article_updates_properties() {
        UUID id = UUID.randomUUID();
        String title = "title";
        LocalDateTime createdDate = LocalDateTime.now();

        String newTitle = "new title";
        String precis = "precis";
        String body = "body";

        Article testArticle = Article.create(new CreateArticleCommand(id, title, createdDate));
        testArticle.apply(new UpdateArticleCommand(id, newTitle, precis, body));

        assertThat(testArticle.getId()).isEqualTo(id);
        assertThat(testArticle.getTitle()).isEqualTo(newTitle);
        assertThat(testArticle.getPrecis()).isEqualTo(precis);
        assertThat(testArticle.getBody()).isEqualTo(body);
        assertThat(testArticle.getState()).isEqualTo(ArticleState.DRAFT);
    }

    @Test
    public void publish_article_checks_ids_match() {
        UUID id = UUID.randomUUID();
        String title = "title";
        LocalDateTime createdDate = LocalDateTime.now();

        String precis = "precis";
        String body = "body";

        UUID otherId = UUID.randomUUID();

        Article testArticle = Article.create(new CreateArticleCommand(id, title, createdDate));
        testArticle.apply(new UpdateArticleCommand(id, title, precis, body));

        assertThatIllegalArgumentException().isThrownBy(() -> {
            testArticle.apply(new PublishArticleCommand(otherId, LocalDateTime.now()));
        });
    }

    @Test
    public void publish_article_must_have_precis() {
        UUID id = UUID.randomUUID();
        String title = "title";
        LocalDateTime createdDate = LocalDateTime.now();

        String precis = null;
        String body = "body";

        Article testArticle = Article.create(new CreateArticleCommand(id, title, createdDate));
        testArticle.apply(new UpdateArticleCommand(id, title, precis, body));

        assertThatIllegalStateException().isThrownBy(() -> {
            testArticle.apply(new PublishArticleCommand(id, LocalDateTime.now()));
        });
    }

    @Test
    public void publish_article_must_have_body() {
        UUID id = UUID.randomUUID();
        String title = "title";
        LocalDateTime createdDate = LocalDateTime.now();

        String precis = "title";
        String body = null;

        Article testArticle = Article.create(new CreateArticleCommand(id, title, createdDate));
        testArticle.apply(new UpdateArticleCommand(id, title, precis, body));

        assertThatIllegalStateException().isThrownBy(() -> {
            testArticle.apply(new PublishArticleCommand(id, LocalDateTime.now()));
        });
    }

    @Test
    public void publish_article_updates_properties() {
        UUID id = UUID.randomUUID();
        String title = "title";
        LocalDateTime createdDate = LocalDateTime.now();

        String precis = "title";
        String body = "body";
        LocalDateTime publishDate = LocalDateTime.now();

        Article testArticle = Article.create(new CreateArticleCommand(id, title, createdDate));
        testArticle.apply(new UpdateArticleCommand(id, title, precis, body));
        testArticle.apply(new PublishArticleCommand(id, publishDate));

        assertThat(testArticle.getId()).isEqualTo(id);
        assertThat(testArticle.getTitle()).isEqualTo(title);
        assertThat(testArticle.getPrecis()).isEqualTo(precis);
        assertThat(testArticle.getBody()).isEqualTo(body);
        assertThat(testArticle.getCreatedTimestamp()).isEqualTo(createdDate);
        assertThat(testArticle.getState()).isEqualTo(ArticleState.PUBLISHED);
        assertThat(testArticle.getPublicationDate()).isEqualTo(publishDate);

    }
}
