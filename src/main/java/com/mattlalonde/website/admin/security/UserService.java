package com.mattlalonde.website.admin.security;

import com.mattlalonde.website.admin.security.domain.User;
import com.mattlalonde.website.admin.security.domain.commands.CreateUserCommand;
import com.mattlalonde.website.admin.security.domain.commands.DeleteUserRefreshTokenCommand;
import com.mattlalonde.website.admin.security.domain.commands.UpdateUserRefreshTokenCommand;

import java.util.UUID;

public interface UserService {
    Boolean existsByEmail(String email);
    User createUser(CreateUserCommand command);
    UUID updateUserRefreshToken(UpdateUserRefreshTokenCommand command);
    void deleteRefreshToken(DeleteUserRefreshTokenCommand command);
    User getUserFromRefreshToken(UUID refreshToken);
}
