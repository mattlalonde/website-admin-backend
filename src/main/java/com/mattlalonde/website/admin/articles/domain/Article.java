package com.mattlalonde.website.admin.articles.domain;

import com.mattlalonde.website.admin.articles.domain.commands.*;
import com.mattlalonde.website.admin.tags.domain.Tag;
import lombok.*;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.*;

@Entity
@NoArgsConstructor
@Getter
@Setter(AccessLevel.PRIVATE)
@Table(name = "articles")
public class Article implements Serializable {

    @Id
    private UUID id;

    @NotNull
    @Column(name = "created_timestamp")
    private LocalDateTime createdTimestamp;

    @Column(name = "publication_date")
    private LocalDateTime publicationDate;

    @Enumerated(EnumType.STRING)
    private ArticleState state;

    @NotEmpty
    private String title;

    @NotEmpty(message = "Article must have a precis to publish", groups = ArticlePublicationChecks.class)
    private String precis;

    @NotEmpty(message = "Article must have a body to publish", groups = ArticlePublicationChecks.class)
    private String body;

    @OneToMany(mappedBy = "article", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ArticleTag> tags = new ArrayList<>();

    public static Article create(CreateArticleCommand command) {
        Assert.notNull(command.getId(), "Article id must be provided");
        Assert.hasText(command.getTitle(), "Title must contain text");
        Assert.notNull(command.getTimeStamp(), "Timestamp must be provided");

        Article result = new Article();
        result.setId(command.getId());
        result.setTitle(command.getTitle());
        result.setCreatedTimestamp(command.getTimeStamp());
        result.setState(ArticleState.DRAFT);

        return result;
    }

    public void apply(UpdateArticleCommand command) {
        Assert.isTrue(id == command.getId(), "Error updating Article, id's must match");
        Assert.hasText(command.getTitle(), "Title must contain text");
        Assert.state(getState() != ArticleState.PUBLISHED, "Cannot update a published article.");

        setTitle(command.getTitle());
        setPrecis(command.getPrecis());
        setBody(command.getBody());
    }

    public void apply(PublishArticleCommand command) {
        Assert.isTrue(id == command.getId(), "Error publishing article, id's must match");
        Assert.state(getTitle() != null && getTitle().length() > 0, "article must have title to publish");
        Assert.state(getPrecis() != null && getPrecis().length() > 0, "article must have precis to publish");
        Assert.state(getBody() != null && getBody().length() > 0, "article must have body to publish");

        setState(ArticleState.PUBLISHED);
        setPublicationDate(command.getPublishDate());
    }

    public void apply(UnpublishArticleCommand command) {
        Assert.isTrue(id == command.getId(), "Error unpublishing article, id's must match");
        Assert.state(getState() == ArticleState.PUBLISHED, "Cannot unpublish article as it is not currently published.");

        setState(ArticleState.DRAFT);
        setPublicationDate(null);
    }

    public void apply(DeleteArticleCommand command) {
        Assert.isTrue(id == command.getId(), "Error deleting article, id's must match");

        setState(ArticleState.DELETED);
    }

    public void apply(ReinstateArticleCommand command) {
        Assert.isTrue(id == command.getId(), "Error reinstating article, id's must match");
        Assert.state(getState() == ArticleState.DELETED, "Cannot reinstate article as it's not currently deleted");

        setState(ArticleState.DRAFT);
    }

    public void apply(AddArticleTagCommand command) {
        Assert.isTrue(id == command.getArticleId(), "Error adding tag to article. Id mismatch");

        if(tags.stream().noneMatch(tag -> tag.getArticleId().equals(getId()) && tag.getTagId().equals(command.getTagId()))) {
            ArticleTag articleTag = new ArticleTag(getId(), command.getTagId(), this);
            tags.add(articleTag);
        }
    }

    public void apply(RemoveArticleTagCommand command) {
        Assert.isTrue(id == command.getArticleId(), "Error removing tag from article. Id mismatch");

        tags.removeIf(tag -> tag.getArticleId().equals(getId()) && tag.getTagId().equals(command.getTagId()));
    }
}
