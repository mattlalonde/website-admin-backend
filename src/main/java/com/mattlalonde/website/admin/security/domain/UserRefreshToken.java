package com.mattlalonde.website.admin.security.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "user_refresh_tokens")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRefreshToken {
    @Id
    private UUID id;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @NotNull
    private LocalDateTime expiresAt;

}
