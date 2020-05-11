package com.mattlalonde.website.admin.tags;

import com.mattlalonde.website.admin.articles.ArticleController;
import com.mattlalonde.website.admin.common.exceptions.EntityNotFoundException;
import com.mattlalonde.website.admin.tags.domain.Tag;
import com.mattlalonde.website.admin.tags.domain.commands.CreateTagCommand;
import com.mattlalonde.website.admin.tags.domain.commands.UpdateTagCommand;
import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;
import java.util.UUID;

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
    public Tag createTag(@RequestBody @Valid CreateTagRequest request){
        return tagService.createTag(new CreateTagCommand(UUID.randomUUID(), request.getName(), request.getDescription()));
    }

    @Getter
    @Setter
    public static class UpdateTagRequest {
        @NotEmpty private String name;
        private String description;
    }

    @PutMapping("/api/tag/{id}/update")
    public Tag updateTag(@PathVariable("id") String id, @RequestBody @Valid UpdateTagRequest request) {
        return tagService.updateTag(new UpdateTagCommand(UUID.fromString(id), request.getName(), request.getDescription()));
    }

    @GetMapping("/api/tag/all")
    public List<Tag> getAllTags() {
        return tagService.getAll();
    }

    @GetMapping("/api/tag/search/{searchTerm}")
    public List<Tag> searchForTagsWithNameContaining(@PathVariable("searchTerm") String searchTerm) {
        return tagService.searchForNameContaining(searchTerm);
    }

    @GetMapping("/api/tag/{id}")
    public Tag getById(@PathVariable("id") String id) throws EntityNotFoundException {
        return tagService.getById(UUID.fromString(id));
    }
}
