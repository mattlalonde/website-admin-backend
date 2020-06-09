package com.mattlalonde.website.admin.tags;

import com.mattlalonde.website.admin.tags.domain.Tag;
import com.mattlalonde.website.admin.tags.domain.commands.CreateTagCommand;
import com.mattlalonde.website.admin.tags.domain.commands.UpdateTagCommand;

import java.util.List;
import java.util.UUID;

public interface TagService {
    Tag createTag(CreateTagCommand command);
    Tag updateTag(UpdateTagCommand command);

    List<Tag> getAll();
    List<Tag> get(List<UUID> tagIds);
    List<Tag> searchForNameContaining(String searchTerm, List<UUID> excludeIds);
    Tag getById(UUID id);
}
