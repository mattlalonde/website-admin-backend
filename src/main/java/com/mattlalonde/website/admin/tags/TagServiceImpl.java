package com.mattlalonde.website.admin.tags;

import com.mattlalonde.website.admin.common.exceptions.EntityNotFoundException;
import com.mattlalonde.website.admin.tags.domain.Tag;
import com.mattlalonde.website.admin.tags.domain.commands.CreateTagCommand;
import com.mattlalonde.website.admin.tags.domain.commands.UpdateTagCommand;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    public TagServiceImpl(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    @Override
    public Tag createTag(CreateTagCommand command) {
        Tag newTag = Tag.create(command);
        return tagRepository.save(newTag);
    }

    @Override
    public Tag updateTag(UpdateTagCommand command) {
        Tag tag = tagRepository.findById(command.getId())
                                .orElseThrow(() -> new EntityNotFoundException(Tag.class, "id", command.getId().toString()));

        tag.apply(command);
        return tagRepository.save(tag);
    }

    @Override
    public List<Tag> getAll() {
        return tagRepository.findAll(Sort.by(Sort.Order.asc("name").ignoreCase()));
    }

    @Override
    public List<Tag> get(List<UUID> tagIds) {
        return tagRepository.findAllById(tagIds);
    }

    @Override
    public List<Tag> searchForNameContaining(String searchTerm, List<UUID> excludeIds) {

        if(excludeIds.isEmpty()){
            return tagRepository.findByNameContainingIgnoreCase(searchTerm, Sort.by(Sort.Order.asc("name").ignoreCase()));
        }
        else {
            return tagRepository.findByNameContainingIgnoreCaseAndIdNotIn(searchTerm, excludeIds, Sort.by(Sort.Order.asc("name").ignoreCase()));
        }
    }

    @Override
    public Tag getById(UUID id) {
        return tagRepository.findById(id)
                            .orElseThrow(() -> new EntityNotFoundException(Tag.class, "id", id.toString()));
    }
}
