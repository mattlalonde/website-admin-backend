package com.mattlalonde.website.admin.articles.dtos;

import com.mattlalonde.website.admin.tags.dtos.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ArticleListEntitiesDTO {
    private Map<UUID, ArticleListItemDTO> articles;
    private Map<UUID, TagDTO> tags;
}
