package com.mattlalonde.website.admin.security.domain;

import com.mattlalonde.website.admin.security.domain.commands.CreateUserCommand;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.NaturalId;
import org.springframework.util.Assert;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = { "email" })
})
@NoArgsConstructor
@Getter
@Setter
public class User {
        @Id
        private UUID id;

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
        @Size(max = 100)
        private String password;

        @ManyToMany(fetch = FetchType.LAZY)
        @JoinTable(name = "user_roles",
                joinColumns = @JoinColumn(name = "user_id"),
                inverseJoinColumns = @JoinColumn(name = "role_id"))
        private Set<Role> roles = new HashSet<>();

        public static User create(CreateUserCommand command) {
                Assert.notNull(command.getId(), "id must be set to create user");
                Assert.hasText(command.getFirstName(), "user must have a first name");
                Assert.hasText(command.getLastName(), "user must have a last name");
                Assert.hasText(command.getEmail(), "user must have an email");
                Assert.hasText(command.getPassword(), "password must be set for user");

                User result = new User();
                result.setId(command.getId());
                result.setFirstName(command.getFirstName());
                result.setLastName(command.getLastName());
                result.setEmail(command.getEmail());
                result.setPassword(command.getPassword());

                return result;
        }
}
