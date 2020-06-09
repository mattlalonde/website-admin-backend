package com.mattlalonde.website.admin.tags.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class TagListDTO {
    private TagListEntitiesDTO entities;
    private List<UUID> result;
}
