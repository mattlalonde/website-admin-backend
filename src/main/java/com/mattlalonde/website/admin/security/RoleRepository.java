package com.mattlalonde.website.admin.security;

import com.mattlalonde.website.admin.security.domain.Role;
import com.mattlalonde.website.admin.security.domain.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByName(RoleName roleName);
}
