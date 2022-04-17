package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.model.CartItem;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.MessageResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.cart.CartAddPizzaRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.cart.CartItemResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.pizza.PizzaResponseDTO;
import hu.dominikvaradi.pizzaorderapp.service.CartService;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("/api/user/{userId}/cart")
@RestController
public class CartController {
    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @Operation(summary = "Get a user's cart's items by given user id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully found user and it's cart with it's items by given user id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PizzaResponseDTO[].class)))
        }),
        @ApiResponse(responseCode = "404", description = "User or it's cart not found with given user id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @GetMapping
    public ResponseEntity<?> getCartItems(@PathVariable long userId) {
        try {
            List<CartItem> cartItems =  cartService.getCartItemsByUserId(userId);

            return ResponseEntity.ok(cartItems
                .stream()
                .map(CartItemResponseDTO::build)
                .collect(Collectors.toList()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Delete each cart item of a user's cart by given user id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully dumped all items of the cart."),
        @ApiResponse(responseCode = "404", description = "User or it's cart not found by given user id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @DeleteMapping
    public ResponseEntity<?> dumpAllCartItems(@PathVariable long userId) {
        try {
            cartService.dumpAllCartItemsByUserId(userId);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Add new items to a user's cart by given user id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully added items to the cart."),
        @ApiResponse(responseCode = "404", description = "User or it's cart not found by given user id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @PostMapping("/items")
    public ResponseEntity<?> createNewCartItem(@PathVariable long userId, @Valid @RequestBody CartAddPizzaRequestDTO cartItemWithCountToAdd) {
        try {
            cartService.createNewCartItemByUserId(userId, cartItemWithCountToAdd);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Delete an item of user's cart by given user id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Successfully deleted the item."),
        @ApiResponse(responseCode = "404", description = "User or it's cart not found with given user id, or the cart item not found with given cart item id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @DeleteMapping("/items/{cartItemId}")
    public ResponseEntity<?> deleteCartItemById(@PathVariable long userId, @PathVariable long cartItemId) {
        try {
            cartService.deleteCartItemByUserIdAndItemId(userId, cartItemId);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Get the size of a user's cart by given user id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully get the cart's size.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = int.class)))
        }),
        @ApiResponse(responseCode = "404", description = "User or it's cart not found with given user id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @GetMapping("/size")
    public ResponseEntity<?> getSizeOfCart(@PathVariable long userId) {
        try {
            return ResponseEntity.ok(cartService.getCartSizeByUserId(userId));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }
}
