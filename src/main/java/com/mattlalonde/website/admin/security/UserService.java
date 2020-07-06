package com.mattlalonde.website.admin.security;

import com.mattlalonde.website.admin.security.domain.User;
import com.mattlalonde.website.admin.security.domain.commands.CreateUserCommand;

public interface UserService {
    Boolean existsByEmail(String email);
    User createUser(CreateUserCommand command);
}
