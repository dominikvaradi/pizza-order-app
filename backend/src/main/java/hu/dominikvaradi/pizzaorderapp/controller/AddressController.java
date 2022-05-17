package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.dto.address.AddressEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.address.AddressResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Address;
import hu.dominikvaradi.pizzaorderapp.service.AddressService;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin({"http://192.168.1.10:4200", "http://localhost:4200"})
@RequestMapping("/api/addresses")
@RestController
public class AddressController {
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @Operation(summary = "Get an address by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found address with given id.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Address not found with given id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#addressId, 'address')")
    @GetMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> getAddressById(@PathVariable long addressId) {
        try {
            Address address = addressService.getAddressById(addressId);

            return ResponseEntity.ok(AddressResponseDTO.build(address));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Edit an address by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Found address with given id, and successfully edited.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = AddressResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Address not found with given id.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#addressId, 'address')")
    @PutMapping("/{addressId}")
    public ResponseEntity<AddressResponseDTO> editAddressById(@PathVariable long addressId, @Valid @RequestBody AddressEditRequestDTO address) {
        try {
            Address editedAddress = addressService.editAddressById(addressId, address);

            return ResponseEntity.ok(AddressResponseDTO.build(editedAddress));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Delete an address by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address found by id, and successfully deleted.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "404", description = "Address not found with given id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasPermission(#addressId, 'address')")
    @DeleteMapping("/{addressId}")
    public ResponseEntity<Void> deleteAddressById(@PathVariable long addressId) {
        try {
            addressService.deleteAddressById(addressId);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
