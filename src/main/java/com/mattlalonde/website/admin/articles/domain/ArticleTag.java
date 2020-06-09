package com.mattlalonde.website.admin.articles.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import java.io.Serializable;
import java.util.UUID;

@Entity
@Table(name = "article_tags")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@IdClass(ArticleTagId.class)
public class ArticleTag implements Serializable {

    @Id
    @JsonIgnore
    private UUID articleId;

    @Id
    private UUID tagId;

    @ManyToOne
    @JoinColumn(name = "article_id", nullable = false, updatable = false, insertable = false)
    @JsonIgnore
    private Article article;
}
