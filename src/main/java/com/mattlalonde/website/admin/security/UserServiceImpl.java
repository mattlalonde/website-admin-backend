package com.mattlalonde.website.admin.security;

import com.mattlalonde.website.admin.common.exceptions.AppException;
import com.mattlalonde.website.admin.common.exceptions.EntityNotFoundException;
import com.mattlalonde.website.admin.security.domain.Role;
import com.mattlalonde.website.admin.security.domain.RoleName;
import com.mattlalonde.website.admin.security.domain.User;
import com.mattlalonde.website.admin.security.domain.UserRefreshToken;
import com.mattlalonde.website.admin.security.domain.commands.CreateUserCommand;
import com.mattlalonde.website.admin.security.domain.commands.DeleteUserRefreshTokenCommand;
import com.mattlalonde.website.admin.security.domain.commands.UpdateUserRefreshTokenCommand;
import com.mattlalonde.website.admin.security.exceptions.UserEmailTakenException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserRefreshTokenRepository userRefreshTokenRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository, UserRefreshTokenRepository userRefreshTokenRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.userRefreshTokenRepository = userRefreshTokenRepository;
    }

    @Override
    public Boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Transactional
    @Override
    public User createUser(CreateUserCommand command) {

        if(userRepository.existsByEmail(command.getEmail())) {
            throw new UserEmailTakenException("Email: '" + command.getEmail() + "' already taken.");
        }

        User newUser = User.create(command);

        Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                            .orElseThrow(() -> new AppException("User Role not set."));

        newUser.setRoles(Collections.singleton(userRole));

        return userRepository.save(newUser);
    }

    @Transactional
    @Override
    public UUID updateUserRefreshToken(UpdateUserRefreshTokenCommand command) {
        if(command.getOldRefreshToken().isPresent()) {
            userRefreshTokenRepository.deleteById(command.getOldRefreshToken().get());
        }

        UUID newRefreshToken = command.getNewRefreshToken();
        User user = userRepository.findById(command.getUserId()).orElseThrow(() -> new EntityNotFoundException(User.class, "id", command.getUserId().toString()));
        LocalDateTime expiresAt = command.getExpiresAt();

        UserRefreshToken newToken = new UserRefreshToken(newRefreshToken, user, expiresAt);
        userRefreshTokenRepository.save(newToken);

        return newRefreshToken;
    }

    @Transactional
    @Override
    public void deleteRefreshToken(DeleteUserRefreshTokenCommand command) {
        userRefreshTokenRepository.deleteById(command.getRefreshTokenId());
    }

    @Transactional
    @Override
    public User getUserFromRefreshToken(UUID refreshToken) {
        UserRefreshToken token = userRefreshTokenRepository.findById(refreshToken).orElseThrow(() -> new EntityNotFoundException(UserRefreshToken.class, "id", refreshToken.toString()));

        return token.getUser();
    }
}
