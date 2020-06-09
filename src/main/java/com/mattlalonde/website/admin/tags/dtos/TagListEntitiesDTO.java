package com.mattlalonde.website.admin.tags.dtos;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.UUID;

@Getter
@AllArgsConstructor
public class TagListEntitiesDTO {
    private Map<UUID, TagDTO> tags;
}
