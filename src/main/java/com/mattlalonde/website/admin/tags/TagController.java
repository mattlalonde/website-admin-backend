package com.mattlalonde.website.admin.tags;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.mattlalonde.website.admin.common.exceptions.EntityNotFoundException;
import com.mattlalonde.website.admin.tags.domain.Tag;
import com.mattlalonde.website.admin.tags.domain.commands.CreateTagCommand;
import com.mattlalonde.website.admin.tags.domain.commands.UpdateTagCommand;
import com.mattlalonde.website.admin.tags.dtos.TagDTO;
import com.mattlalonde.website.admin.tags.dtos.TagListDTO;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
public class TagController {

    private final TagService tagService;

    public TagController(TagService tagService) {
        this.tagService = tagService;
    }

    @Getter
    @Setter
    public static class CreateTagRequest {
        @NotEmpty private String name;
        private String description;
    }

    @PostMapping("/api/tag/create")
    public TagDTO createTag(@RequestBody @Valid CreateTagRequest request){
        Tag tag = tagService.createTag(new CreateTagCommand(UUID.randomUUID(), request.getName(), request.getDescription()));

        return TagDtoBuilder.buildTagDto(tag);
    }

    @Getter
    @Setter
    public static class UpdateTagRequest {
        @NotEmpty private String name;
        private String description;
    }

    @PutMapping("/api/tag/{id}/update")
    public TagDTO updateTag(@PathVariable("id") String id, @RequestBody @Valid UpdateTagRequest request) {
        Tag tag = tagService.updateTag(new UpdateTagCommand(UUID.fromString(id), request.getName(), request.getDescription()));

        return TagDtoBuilder.buildTagDto(tag);
    }

    @GetMapping("/api/tag/all")
    public TagListDTO getAllTags() {
        List<Tag> tags = tagService.getAll();

        return TagDtoBuilder.buildTagList(tags);
    }

    @Getter
    @Setter
    public static class SearchTagsRequest {
        @NotEmpty private String searchTerm;

        @JsonProperty(required = false)
        private Optional<List<String>> excludeIds = Optional.empty();
    }

    @PostMapping("/api/tag/search")
    public List<Tag> searchForTagsWithNameContaining(@RequestBody @Valid SearchTagsRequest request) {

        return tagService.searchForNameContaining(request.searchTerm, request.excludeIds.orElse(Collections.emptyList()).stream().map(i -> UUID.fromString(i)).collect(Collectors.toList()));
    }

    @GetMapping("/api/tag/{id}")
    public TagDTO getById(@PathVariable("id") String id) throws EntityNotFoundException {
        Tag tag = tagService.getById(UUID.fromString(id));

        return TagDtoBuilder.buildTagDto(tag);
    }
}
