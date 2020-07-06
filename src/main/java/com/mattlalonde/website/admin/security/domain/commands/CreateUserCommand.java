package com.mattlalonde.website.admin.security.domain.commands;

import lombok.NonNull;
import lombok.Value;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.UUID;

@Value
public class CreateUserCommand {
    @NonNull
    private UUID id;

    @NotEmpty
    @Size(max = 50)
    private String firstName;

    @NotEmpty
    @Size(max = 50)
    private String lastName;

    @NotEmpty
    @Size(max = 250)
    @Email
    private String email;

    @NotEmpty
    @Size(max = 20)
    private String password;
}
