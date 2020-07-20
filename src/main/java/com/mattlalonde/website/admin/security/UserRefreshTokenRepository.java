package com.mattlalonde.website.admin.security;

import com.mattlalonde.website.admin.security.domain.UserRefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface UserRefreshTokenRepository extends JpaRepository<UserRefreshToken, UUID> {
}
