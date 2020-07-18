package com.mattlalonde.website.admin.security;

import com.mattlalonde.website.admin.security.domain.User;
import com.mattlalonde.website.admin.security.domain.commands.CreateUserCommand;
import com.mattlalonde.website.admin.security.domain.commands.DeleteUserRefreshTokenCommand;
import com.mattlalonde.website.admin.security.domain.commands.UpdateUserRefreshTokenCommand;
import com.mattlalonde.website.admin.security.requests.LoginRequest;
import com.mattlalonde.website.admin.security.requests.SignUpRequest;
import com.mattlalonde.website.admin.security.responses.JwtAuthenticationResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider tokenProvider;

    @Value("${app.refreshTokenExpiresInDays}")
    private int refreshTokenExpiresInDays;

    public AuthController(
            AuthenticationManager authenticationManager,
            UserService userService,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider tokenProvider
    ){
        this.authenticationManager = authenticationManager;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = tokenProvider;
    }

    @PostMapping("/signin")
    public JwtAuthenticationResponse authenticateUser(@Valid @RequestBody LoginRequest loginRequest, HttpServletResponse response) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getEmail(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        String jwt = tokenProvider.generateToken(userPrincipal);

        UUID refreshToken = UUID.randomUUID();
        LocalDateTime refreshTokenExpiry = LocalDateTime.now().plusDays(refreshTokenExpiresInDays);
        userService.updateUserRefreshToken(new UpdateUserRefreshTokenCommand(
                userPrincipal.getId(),
                refreshToken,
                Optional.empty(),
                refreshTokenExpiry
        ));

        Cookie refreshTokenCookie = new Cookie("refresh_token", refreshToken.toString());
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(refreshTokenExpiresInDays * 24 * 60 *60);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);


        return new JwtAuthenticationResponse(jwt);
    }

    @PostMapping("/signup")
    public User registerUser(@Valid @RequestBody SignUpRequest signUpRequest) {
        User user = userService.createUser(new CreateUserCommand(
                UUID.randomUUID(),
                signUpRequest.getFirstName(),
                signUpRequest.getLastName(),
                signUpRequest.getEmail(),
                passwordEncoder.encode(signUpRequest.getPassword())
        ));

        return user;
    }

    @PostMapping("/signout")
    public void signOut(@CookieValue(value = "refresh_token", defaultValue = "") String refreshToken, HttpServletResponse response) {

        if(!refreshToken.trim().isEmpty()) {
            userService.deleteRefreshToken(new DeleteUserRefreshTokenCommand(UUID.fromString(refreshToken)));
        }

        Cookie refreshTokenCookie = new Cookie("refresh_token", null);
        refreshTokenCookie.setHttpOnly(true);
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setPath("/");
        response.addCookie(refreshTokenCookie);
    }

    @PostMapping("/token/refresh")
    public JwtAuthenticationResponse refreshAccessToken(@CookieValue(value = "refresh_token", defaultValue = "") String oldRefreshToken, HttpServletResponse response) throws IOException {

        if(!oldRefreshToken.trim().isEmpty()) {
            User user = userService.getUserFromRefreshToken(UUID.fromString(oldRefreshToken));

            String jwt = tokenProvider.generateToken(user);

            UUID newRefreshToken = UUID.randomUUID();
            LocalDateTime refreshTokenExpiry = LocalDateTime.now().plusDays(refreshTokenExpiresInDays);
            userService.updateUserRefreshToken(new UpdateUserRefreshTokenCommand(
                    user.getId(),
                    newRefreshToken,
                    Optional.of(UUID.fromString(oldRefreshToken)),
                    refreshTokenExpiry
            ));

            Cookie refreshTokenCookie = new Cookie("refresh_token", newRefreshToken.toString());
            refreshTokenCookie.setHttpOnly(true);
            refreshTokenCookie.setMaxAge(refreshTokenExpiresInDays * 24 * 60 *60);
            refreshTokenCookie.setPath("/");
            response.addCookie(refreshTokenCookie);

            return new JwtAuthenticationResponse(jwt);
        }

        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Could not authorize user, no refresh token.");
        return null;
    }
}
