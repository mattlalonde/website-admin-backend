package com.mattlalonde.website.admin.articles.dtos;

import com.mattlalonde.website.admin.tags.dtos.TagDTO;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Map;
import java.util.UUID;

@Data
@AllArgsConstructor
public class ArticleDTO {
    private ArticleDetailsDTO article;
    private Map<UUID, TagDTO> tags;
}
