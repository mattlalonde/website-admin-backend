package com.mattlalonde.website.admin.security.domain.commands;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Value
public class UpdateUserRefreshTokenCommand {

    @NotNull
    private UUID userId;

    @NotNull
    private UUID newRefreshToken;

    private Optional<UUID> oldRefreshToken;

    @NotNull
    private LocalDateTime expiresAt;
}
