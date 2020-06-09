package com.mattlalonde.website.admin.tags;

import com.mattlalonde.website.admin.tags.domain.Tag;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface TagRepository extends JpaRepository<Tag, UUID>, JpaSpecificationExecutor<Tag> {

    List<Tag> findByNameContainingIgnoreCaseAndIdNotIn(String searchTerm, List<UUID> excludeIds, Sort sort);
    List<Tag> findByNameContainingIgnoreCase(String searchTerm, Sort sort);
}
