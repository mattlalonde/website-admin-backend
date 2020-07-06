package com.mattlalonde.website.admin.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.UUID;

public interface AppUserDetailsService extends UserDetailsService {
    UserDetails loadUserById(UUID id);
}
