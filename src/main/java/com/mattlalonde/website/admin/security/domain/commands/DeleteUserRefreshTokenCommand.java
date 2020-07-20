package com.mattlalonde.website.admin.security.domain.commands;

import lombok.Value;

import javax.validation.constraints.NotNull;
import java.util.UUID;

@Value
public class DeleteUserRefreshTokenCommand {
    @NotNull
    private UUID refreshTokenId;
}
