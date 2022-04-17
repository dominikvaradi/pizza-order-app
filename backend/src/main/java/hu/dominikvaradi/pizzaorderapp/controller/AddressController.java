package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.model.Address;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.MessageResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.address.AddressResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.address.AddressSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.service.AddressService;
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
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.stream.Collectors;

@RestController
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "Get a user's addresses.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found user with given id, and it's addresses.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AddressResponseDTO[].class)))
        }),
        @ApiResponse(responseCode = "404", description = "User not found with given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @GetMapping("/api/user/{userId}/addresses")
    public ResponseEntity<?> getUsersAddresses(@PathVariable long userId) {
        try {
            return ResponseEntity.ok(addressService.getAllAddressesByUserId(userId)
                .stream()
                .map(AddressResponseDTO::build)
                .collect(Collectors.toList()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Create a new address for a user.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Found user with given id, and the address successfully created and added to the user.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AddressResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "404", description = "User not found with given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @PostMapping("/api/user/{userId}/addresses")
    public ResponseEntity<?> createNewAddressForUser(@PathVariable long userId, @Valid @RequestBody AddressSaveRequestDTO address) {
        try {
            Address newAddress = addressService.createNewAddress(userId, address);
            return ResponseEntity.created(URI.create("/api/address/" + newAddress.getId()))
                .body(AddressResponseDTO.build(newAddress));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Get an address by id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Found address with given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = AddressResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "404", description = "Address not found with given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @GetMapping("/api/address/{addressId}")
    public ResponseEntity<?> getAddressById(@PathVariable long addressId) {
        try {
            Address address = addressService.getAddressById(addressId);

            return ResponseEntity.ok(AddressResponseDTO.build(address));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Edit an address by id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Found address with given id, and successfully edited."),
        @ApiResponse(responseCode = "404", description = "Address not found with given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @PutMapping("/api/address/{addressId}")
    public ResponseEntity<?> editAddressById(@PathVariable long addressId, @Valid @RequestBody AddressSaveRequestDTO address) {
        try {
            addressService.editAddressById(addressId, address);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Delete an address by id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Address found by id, and successfully deleted."),
        @ApiResponse(responseCode = "404", description = "Address not found with given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @DeleteMapping("/api/address/{addressId}")
    public ResponseEntity<?> deleteAddressById(@PathVariable long addressId) {
        try {
            addressService.deleteAddressById(addressId);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }
}
