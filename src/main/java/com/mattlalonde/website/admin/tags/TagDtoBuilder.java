package com.mattlalonde.website.admin.tags;

import com.mattlalonde.website.admin.tags.domain.Tag;
import com.mattlalonde.website.admin.tags.dtos.TagDTO;
import com.mattlalonde.website.admin.tags.dtos.TagListDTO;
import com.mattlalonde.website.admin.tags.dtos.TagListEntitiesDTO;

import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

public final class TagDtoBuilder {
    public static TagDTO buildTagDto(Tag tag) {
        return new TagDTO(tag.getId(), tag.getName(), tag.getDescription());
    }


    public static TagListDTO buildTagList(List<Tag> tags) {
        Map<UUID, TagDTO> tagDtos = tags.stream().map(tag -> buildTagDto(tag)).collect(Collectors.toMap(k -> k.getId(), v -> v));
        List<UUID> orderedResults = tags.stream().map(tag -> tag.getId()).collect(Collectors.toList());

        return new TagListDTO(new TagListEntitiesDTO(tagDtos), orderedResults);
    }
}
