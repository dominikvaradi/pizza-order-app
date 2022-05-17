package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.dto.security.JwtResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.security.RefreshTokenRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.security.RefreshTokenResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserLoginRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.RefreshToken;
import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.security.jwt.JwtUtils;
import hu.dominikvaradi.pizzaorderapp.security.service.UserDetailsImpl;
import hu.dominikvaradi.pizzaorderapp.service.RefreshTokenService;
import hu.dominikvaradi.pizzaorderapp.service.exception.RefreshTokenException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin({"http://192.168.1.10:4200", "http://localhost:4200"})
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    @Value("${app.jwt.tokenPrefixName}")
    private String jwtPrefixName;

    private final JwtUtils jwtUtils;

    private final AuthenticationManager authenticationManager;

    private final RefreshTokenService refreshTokenService;

    public AuthController(JwtUtils jwtUtils, AuthenticationManager authenticationManager, RefreshTokenService refreshTokenService) {
        this.jwtUtils = jwtUtils;
        this.authenticationManager = authenticationManager;
        this.refreshTokenService = refreshTokenService;
    }

    @Operation(summary = "Login with a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user with given username and password pair, and successfully logged in.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = JwtResponseDTO.class))
            }),
            @ApiResponse(responseCode = "401", description = "User not found with given username and password pair.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "404", description = "User not found while creating the refresh token.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PostMapping ("/login")
    public ResponseEntity<JwtResponseDTO> loginUser(@Valid @RequestBody UserLoginRequestDTO user) {
        Authentication authentication = authenticationManager
            .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetailsImpl userPrincipal = (UserDetailsImpl)authentication.getPrincipal();
        String jwtToken = jwtUtils.generateTokenFromUsername(userPrincipal.getUsername());

        try {
            RefreshToken refreshToken = refreshTokenService.createRefreshToken(userPrincipal.getUser().getId());

            return ResponseEntity.ok().body(
                new JwtResponseDTO(
                        jwtToken,
                        refreshToken.getToken(),
                        jwtPrefixName,
                        UserResponseDTO.build(userPrincipal.getUser())
                )
            );
        } catch (RefreshTokenException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Creates a new JWT access token by a refresh token.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "New JWT access token created successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = RefreshTokenResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "401", description = "Refresh token has not found or expired.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PostMapping("/refresh-token")
    public ResponseEntity<RefreshTokenResponseDTO> refreshJwtToken(@Valid @RequestBody RefreshTokenRequestDTO request) {
        try {
            RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                .orElseThrow(() -> new RefreshTokenException("Refresh token has not found!"));

            refreshTokenService.verifyExpiration(refreshToken);

            User user = refreshToken.getUser();
            String newJwtToken = jwtUtils.generateTokenFromUsername(user.getUsername());

            return ResponseEntity.ok(new RefreshTokenResponseDTO(newJwtToken, refreshToken.getToken(), jwtPrefixName));
        } catch (RefreshTokenException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
