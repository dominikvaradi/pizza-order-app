package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.MessageResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.user.UserResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.user.UserSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.service.UserService;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class UserController {
    private final UserService userService;

    private final PasswordEncoder passwordEncoder;

    public UserController(UserService userService, PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    @Operation(summary = "Get all users.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All users.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO[].class)))
        })
    })
    @GetMapping("/api/users")
    public ResponseEntity<?> getAllUsers() {
        List<User> users = userService.getAllUsers();

        return ResponseEntity.ok(users.stream()
            .map(UserResponseDTO::build)
            .collect(Collectors.toList()));
    }

    @Operation(summary = "Get a user by given id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User by given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "404", description = "User not found with given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @GetMapping("/api/user/{userId}")
    public ResponseEntity<?> getUserById(@PathVariable long userId) {
        try {
            User user = userService.getUserById(userId);

            return ResponseEntity.ok(UserResponseDTO.build(user));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Edit a user by given id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User with given id successfully edited."),
        @ApiResponse(responseCode = "404", description = "User not found with given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Provided request is invalid, or username or email are already in use.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @PutMapping("/api/user/{userId}")
    public ResponseEntity<?> editUserById(@PathVariable long userId, @Valid @RequestBody UserSaveRequestDTO user) {
        try {
            userService.editUserById(userId, user, passwordEncoder);

            return ResponseEntity.noContent().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponseDTO(e.getMessage()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Delete a user by given id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "User with given id successfully deleted."),
        @ApiResponse(responseCode = "404", description = "User not found with given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @DeleteMapping("/api/user/{userId}")
    public ResponseEntity<?> deleteUserById(@PathVariable long userId) {
        try {
            userService.deleteUserById(userId);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }
}
