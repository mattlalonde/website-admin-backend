package com.mattlalonde.website.admin.articles.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class ArticleListDTO {
    private ArticleListEntitiesDTO entities;
    private List<UUID> result;
}