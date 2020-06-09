package com.mattlalonde.website.admin.articles;

import com.mattlalonde.website.admin.articles.domain.Article;
import com.mattlalonde.website.admin.articles.dtos.*;
import com.mattlalonde.website.admin.tags.domain.Tag;
import com.mattlalonde.website.admin.tags.dtos.TagDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class ArticleDtoBuilder {

    public static ArticleDTO buildDetails(Article article, List<Tag> tags) {
        ArticleDetailsDTO details = new ArticleDetailsDTO();

        details.setId(article.getId());
        details.setCreatedTimestamp(article.getCreatedTimestamp());
        details.setTitle(article.getTitle());
        details.setPrecis(article.getPrecis());
        details.setBody(article.getBody());
        details.setState(article.getState());
        details.setPublicationDate(article.getPublicationDate());

        for(Tag tag : tags) {
            details.getTags().add(tag.getId());
        }

        Map<UUID, TagDTO> tagDtos = tags.stream().map(tag -> new TagDTO(tag.getId(), tag.getName(), tag.getDescription()))
                                                 .collect(Collectors.toMap(k -> k.getId(), v -> v));

        return new ArticleDTO(details, tagDtos);
    }

    public static ArticleListDTO buildList(List<Article> articles, List<Tag> tags) {
        Map<UUID, ArticleListItemDTO> articleDtos = articles.stream().map(article -> {
            ArticleListItemDTO result = new ArticleListItemDTO();

            result.setId(article.getId());
            result.setCreatedTimestamp(article.getCreatedTimestamp());
            result.setTitle(article.getTitle());
            result.setPrecis(article.getPrecis());
            result.setState(article.getState());
            result.setPublicationDate(article.getPublicationDate());

            result.setTags(article.getTags().stream().map(tag -> tag.getTagId()).collect(Collectors.toList()));

            return result;
        }).collect(Collectors.toMap(k -> k.getId(), v -> v));

        List<UUID> orderedResults = articles.stream().map(article -> article.getId()).collect(Collectors.toList());

        Map<UUID, TagDTO> tagDtos = tags.stream()
                                                .map(tag -> new TagDTO(tag.getId(), tag.getName(), tag.getDescription()))
                                                .collect(Collectors.toMap(k -> k.getId(), v -> v));

        return new ArticleListDTO(new ArticleListEntitiesDTO(articleDtos, tagDtos), orderedResults);
    }
}
