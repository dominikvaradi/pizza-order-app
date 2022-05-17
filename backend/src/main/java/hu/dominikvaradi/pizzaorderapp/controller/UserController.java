package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.dto.address.AddressCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.address.AddressResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.order.OrderResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserRegisterRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Address;
import hu.dominikvaradi.pizzaorderapp.data.model.Order;
import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.service.AddressService;
import hu.dominikvaradi.pizzaorderapp.service.OrderService;
import hu.dominikvaradi.pizzaorderapp.service.UserService;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.ConflictException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin({"http://192.168.1.10:4200", "http://localhost:4200"})
@RequestMapping("/api/users")
@RestController
public class UserController {
    private final UserService userService;

    private final AddressService addressService;

    private final PasswordEncoder passwordEncoder;

    private final OrderService orderService;

    public UserController(UserService userService, AddressService addressService, PasswordEncoder passwordEncoder, OrderService orderService) {
        this.userService = userService;
        this.addressService = addressService;
        this.passwordEncoder = passwordEncoder;
        this.orderService = orderService;
    }

    @Operation(summary = "Get all users.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All users.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = UserResponseDTO.class)))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping
    public ResponseEntity<Page<UserResponseDTO>> getAllUsers(
            @RequestParam(required = false) String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> userPage = userService.getAllUsers(term, pageable);

        return ResponseEntity.ok(userPage.map(UserResponseDTO::build));
    }

    @Operation(summary = "Register a new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User registered successfully.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Provided request is invalid, or username or email are already in use.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PostMapping
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRegisterRequestDTO user) {
        try {
            User newUser = userService.createUser(user, passwordEncoder);
            return ResponseEntity.created(URI.create("/api/user/" + newUser.getId()))
                    .body(UserResponseDTO.build(newUser));
        } catch (ConflictException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }
    }

    @Operation(summary = "Get a user by given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User by given id.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found with given id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> getUserById(@PathVariable long userId) {
        try {
            User user = userService.getUserById(userId);

            return ResponseEntity.ok(UserResponseDTO.build(user));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Edit a user by given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User with given id successfully edited.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = UserResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found with given id.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "400", description = "Provided request is invalid, or username or email are already in use.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @PutMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> editUserById(@PathVariable long userId, @Valid @RequestBody UserEditRequestDTO user) {
        try {
            User editedUser = userService.editUserById(userId, user, passwordEncoder);

            return ResponseEntity.ok(UserResponseDTO.build(editedUser));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a user by given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User with given id successfully deleted.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "404", description = "User not found with given id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUserById(@PathVariable long userId) {
        try {
            userService.deleteUserById(userId);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get a user's orders by given user id, optionally with their items (pizzas).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's orders.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "User with given user id has not found.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @Transactional
    @GetMapping("/{userId}/orders")
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrdersByUserId(
            @RequestParam(defaultValue = "false") boolean withItems,
            @PathVariable long userId,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        try {
            Pageable pageable = PageRequest.of(page, size);
            Page<Order> orderPage = orderService.getAllOrdersByUserId(userId, pageable);

            return ResponseEntity.ok(orderPage.map(order -> OrderResponseDTO.build(order, withItems)));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get a user's active order by given user id, optionally with it's items (pizzas).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User's active order", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "User with given user id has not found.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @Transactional
    @GetMapping("/{userId}/active-order")
    public ResponseEntity<OrderResponseDTO> getActiveOrderByUserId(@RequestParam(defaultValue = "false") boolean withItems, @PathVariable long userId) {
        try {
            Order activeOrder = orderService.getActiveOrderByUserId(userId);

            return ResponseEntity.ok(OrderResponseDTO.build(activeOrder, withItems));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get a user's addresses.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user with given id, and it's addresses.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AddressResponseDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "User not found with given id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @Transactional
    @GetMapping("/{userId}/addresses")
    public ResponseEntity<List<AddressResponseDTO>> getUsersAddresses(@PathVariable long userId) {
        try {
            return ResponseEntity.ok(addressService.getAllAddressesByUserId(userId)
                    .stream()
                    .map(AddressResponseDTO::build)
                    .collect(Collectors.toList()));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Create a new address for a user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found user with given id, and the address successfully created and added to the user.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "User not found with given id.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @PostMapping("/{userId}/addresses")
    public ResponseEntity<AddressResponseDTO> createNewAddressForUser(@PathVariable long userId, @Valid @RequestBody AddressCreateRequestDTO address) {
        try {
            Address newAddress = addressService.createNewAddress(userId, address);

            return ResponseEntity.created(URI.create("/api/addresses/" + newAddress.getId()))
                    .body(AddressResponseDTO.build(newAddress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
