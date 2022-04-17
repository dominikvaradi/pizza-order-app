package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.MessageResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.user.UserLoginRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.user.UserResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.user.UserSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.security.jwt.JwtUtils;
import hu.dominikvaradi.pizzaorderapp.security.service.UserDetailsImpl;
import hu.dominikvaradi.pizzaorderapp.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.net.URI;

@RequestMapping("/api/auth")
@RestController
public class AuthController {
    private final UserService userService;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final PasswordEncoder passwordEncoder;

    public AuthController(UserService userService, JwtUtils jwtUtils, AuthenticationManager authenticationManager, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Login with a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found user with given username and password pair, and successfully logged in.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "401", description = "User not found with given username and password pair.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))
        })
    })
    @PostMapping ("/login")
    public ResponseEntity<?> loginUser(@Valid @RequestBody UserLoginRequestDTO user) {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        ResponseCookie jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        return ResponseEntity.ok()
            .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
            .body(new MessageResponseDTO("Successfully logged in with user: " + user.getUsername()));
    }

    @Operation(summary = "Register a new user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "User registered successfully.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Provided request is invalid, or username or email are already in use.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = String.class)))
        })
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody UserSaveRequestDTO user) {
        try {
            User newUser = userService.createUser(user, passwordEncoder);
            return ResponseEntity.created(URI.create("/api/user/" + newUser.getId()))
                .body(UserResponseDTO.build(newUser));
        } catch (Exception e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }
}
