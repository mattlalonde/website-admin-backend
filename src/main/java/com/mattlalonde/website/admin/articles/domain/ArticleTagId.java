package com.mattlalonde.website.admin.articles.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;

public class ArticleTagId implements Serializable {
    @Column(name = "article_id")
    private UUID articleId;

    @Column(name = "tag_id")
    private UUID tagId;
}
