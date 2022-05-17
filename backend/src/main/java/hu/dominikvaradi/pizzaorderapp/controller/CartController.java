package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.dto.cart.CartItemResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.cart.CartItemSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.CartItem;
import hu.dominikvaradi.pizzaorderapp.service.CartService;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin({"http://192.168.1.10:4200", "http://localhost:4200"})
@RequestMapping("/api/cart/{userId}/")
@RestController
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Get a user's cart's items by given user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully found user and it's cart with it's items by given user id.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = CartItemResponseDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "User or it's cart not found with given user id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @GetMapping("/items")
    public ResponseEntity<List<CartItemResponseDTO>> getCartItems(@PathVariable long userId) {
        try {
            List<CartItem> cartItems =  cartService.getCartItemsByUserId(userId);

            return ResponseEntity.ok(cartItems
                .stream()
                .map(CartItemResponseDTO::build)
                .collect(Collectors.toList()));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete each cart item of a user's cart by given user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully dumped all items of the cart.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "404", description = "User or it's cart not found by given user id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @DeleteMapping("/items")
    public ResponseEntity<Void> dumpAllCartItems(@PathVariable long userId) {
        try {
            cartService.dumpAllCartItemsByUserId(userId);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Add new items to a user's cart by given user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully added items to the cart.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "404", description = "User or it's cart not found by given user id.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @PostMapping("/items")
    public ResponseEntity<Void> createNewCartItem(@PathVariable long userId, @Valid @RequestBody CartItemSaveRequestDTO cartItemToSave) {
        try {
            cartService.createNewCartItemByUserId(userId, cartItemToSave);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete an item of user's cart by given user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Successfully deleted the item.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "404", description = "User or it's cart not found with given user id, or the cart item not found with given cart item id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<Void> deleteCartItemById(@PathVariable long userId, @PathVariable long cartItemId) {
        try {
            cartService.deleteCartItemByUserIdAndItemId(userId, cartItemId);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get the size of a user's cart by given user id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successfully get the cart's size.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = int.class))
            }),
            @ApiResponse(responseCode = "404", description = "User or it's cart not found with given user id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or authentication.principal.user.id == #userId")
    @GetMapping("/size")
    public ResponseEntity<Integer> getSizeOfCart(@PathVariable long userId) {
        try {
            return ResponseEntity.ok(cartService.getCartSizeByUserId(userId));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
