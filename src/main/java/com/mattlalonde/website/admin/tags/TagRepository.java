package com.mattlalonde.website.admin.tags;

import com.mattlalonde.website.admin.tags.domain.Tag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID> {
    List<Tag> findByNameContaining(String searchTerm);
}
