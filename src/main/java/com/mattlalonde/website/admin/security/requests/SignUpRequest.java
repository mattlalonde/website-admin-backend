package com.mattlalonde.website.admin.security.requests;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;

@Getter
@Setter
public class SignUpRequest {
    @NotEmpty
    @Size(max = 50)
    private String firstName;

    @NotEmpty
    @Size(max = 50)
    private String lastName;

    @NaturalId
    @NotEmpty
    @Size(max = 250)
    @Email
    private String email;

    @NotEmpty
    @Size(min = 6, max = 20)
    private String password;
}
