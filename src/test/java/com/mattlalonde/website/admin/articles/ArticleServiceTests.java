package com.mattlalonde.website.admin.articles;

import com.mattlalonde.website.admin.articles.domain.Article;
import com.mattlalonde.website.admin.articles.domain.ArticleState;
import com.mattlalonde.website.admin.articles.domain.commands.CreateArticleCommand;
import com.mattlalonde.website.admin.articles.domain.commands.PublishArticleCommand;
import com.mattlalonde.website.admin.articles.domain.commands.UpdateArticleCommand;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.BDDMockito.*;
import static org.assertj.core.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ArticleServiceTests {

    @MockBean
    ArticleRepository articleRepository;

    @Autowired
    ArticleServiceImpl articleService;

    @Test
    public void when_create_article_it_should_return_new_article_and_save() {

        given(articleRepository.save(any(Article.class)))
                .willAnswer(answer -> answer.getArgument(0, Article.class));

        UUID id = UUID.randomUUID();
        String title = "created article title";
        LocalDateTime created = LocalDateTime.now();

        CreateArticleCommand command = new CreateArticleCommand(id, title, created);

        Article newArticle = articleService.createArticle(command);

        then(articleRepository)
                .should(times(1))
                .save(any(Article.class));

        assertThat(newArticle.getId()).isEqualTo(id);
        assertThat(newArticle.getTitle()).isEqualTo(title);
        assertThat(newArticle.getCreatedTimestamp()).isEqualTo(created);

    }

    @Test
    public void when_create_article_it_should_reject_empty_title_and_not_save() {

        UUID id = UUID.randomUUID();
        String title = "";
        LocalDateTime created = LocalDateTime.now();

        CreateArticleCommand command = new CreateArticleCommand(id, title, created);

        assertThatIllegalArgumentException().isThrownBy(() -> {
            Article newArticle = articleService.createArticle(command);
        });

        then(articleRepository)
                .should(times(0))
                .save(any(Article.class));
    }

    @Test
    public void when_update_article_should_return_updated_article_and_save() {
        UUID id = UUID.randomUUID();

        Article testArticle = Article.create(new CreateArticleCommand(id, "title", LocalDateTime.now()));

        given(articleRepository.findById(id)).willReturn(Optional.of(testArticle));
        given(articleRepository.save(any(Article.class))).willAnswer(answer -> answer.getArgument(0, Article.class));

        String newTitle = "new title";
        String newPrecis = "new precis";
        String newBody = "new Body";

        UpdateArticleCommand command = new UpdateArticleCommand(id, newTitle, newPrecis, newBody);

        Article result = articleService.updateArticle(command);

        then(articleRepository)
                .should(times(1))
                .findById(id);
        then(articleRepository)
                .should(times(1))
                .save(any(Article.class));

        assertThat(result.getId()).isEqualTo(id);
        assertThat(result.getTitle()).isEqualTo(newTitle);
        assertThat(result.getBody()).isEqualTo(newBody);
        assertThat(result.getState()).isEqualTo(testArticle.getState());
    }

    @Test
    public void when_publish_article_should_return_updated_article_and_save() {
        UUID id = UUID.randomUUID();

        Article testArticle = Article.create(new CreateArticleCommand(id, "title", LocalDateTime.now()));
        testArticle.apply(new UpdateArticleCommand(id, "title", "precis", "body"));

        given(articleRepository.findById(id)).willReturn(Optional.of(testArticle));
        given(articleRepository.save(any(Article.class))).willAnswer(answer -> answer.getArgument(0, Article.class));

        LocalDateTime publishDate = LocalDateTime.now();

        PublishArticleCommand command = new PublishArticleCommand(id, publishDate);

        Article result = articleService.publishArticle(command);

        then(articleRepository)
                .should(times(1))
                .findById(id);
        then(articleRepository)
                .should(times(1))
                .save(any(Article.class));

        assertThat(result.getState()).isEqualTo(ArticleState.PUBLISHED);
        assertThat(result.getPublicationDate()).isEqualTo(publishDate);
    }
}
