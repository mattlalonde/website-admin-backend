package com.mattlalonde.website.admin.articles.dtos;

import com.mattlalonde.website.admin.articles.domain.ArticleState;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ArticleDetailsDTO {
    private UUID id;
    private LocalDateTime createdTimestamp;
    private LocalDateTime publicationDate;
    private ArticleState state;
    private String title;
    private String precis;
    private String body;
    private List<UUID> tags = new ArrayList<>();
}
