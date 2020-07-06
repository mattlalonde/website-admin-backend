package com.mattlalonde.website.admin.security;

import com.mattlalonde.website.admin.common.exceptions.AppException;
import com.mattlalonde.website.admin.security.domain.Role;
import com.mattlalonde.website.admin.security.domain.RoleName;
import com.mattlalonde.website.admin.security.domain.User;
import com.mattlalonde.website.admin.security.domain.commands.CreateUserCommand;
import com.mattlalonde.website.admin.security.exceptions.UserEmailTakenException;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
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
}
