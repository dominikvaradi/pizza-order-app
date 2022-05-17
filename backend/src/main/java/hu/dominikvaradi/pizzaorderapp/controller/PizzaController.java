package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.dto.pizza.PizzaCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.pizza.PizzaEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.pizza.PizzaResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Pizza;
import hu.dominikvaradi.pizzaorderapp.service.PizzaService;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.InternalServerErrorException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin({"http://192.168.1.10:4200", "http://localhost:4200"})
@RequestMapping("/api/pizzas")
@RestController
public class PizzaController {
    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @Operation(summary = "Get all pizzas.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "All pizzas.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PizzaResponseDTO.class)))
            })
    })
    @GetMapping
    public ResponseEntity<List<PizzaResponseDTO>> getAllPizzas() {
        List<Pizza> pizzas = pizzaService.getAllPizzas();

        return ResponseEntity.ok(pizzas
            .stream()
            .map(PizzaResponseDTO::build)
            .collect(Collectors.toList()));
    }

    @Operation(summary = "Create a new pizza.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Pizza successfully created.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PizzaResponseDTO.class))
            }),
            @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "500", description = "Something happened saving the pizza's image.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PostMapping
    public ResponseEntity<PizzaResponseDTO> createNewPizza(@Valid PizzaCreateRequestDTO pizza, @RequestParam(value = "file") MultipartFile image) {
        try {
            Pizza newPizza = pizzaService.createNewPizza(pizza, image);

            return ResponseEntity.created(URI.create("/api/pizza/" + newPizza.getId()))
                .body(PizzaResponseDTO.build(newPizza));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Get a pizza by given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pizza by given id.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PizzaResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Pizza has not found by given id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @GetMapping("/{pizzaId}")
    public ResponseEntity<PizzaResponseDTO> getPizzaById(@PathVariable long pizzaId) {
        try {
            Pizza pizza = pizzaService.getPizzaById(pizzaId);

            return ResponseEntity.ok(PizzaResponseDTO.build(pizza));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get a pizza's image by given file name.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pizza's image in bytes.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = byte.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Pizza image has not found by given file name.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "500", description = "Something happened getting the pizza's image.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @ResponseBody
    @GetMapping(value = "/{pizzaId}/image", produces = "image/*")
    public ResponseEntity<byte[]> getPizzaImageByFileName(@PathVariable long pizzaId) {
        try {
            Resource image = pizzaService.getPizzaImageById(pizzaId);

            InputStream imageInputStream = image.getInputStream();
            byte[] imageBytes = imageInputStream.readAllBytes();
            imageInputStream.close();

            return ResponseEntity.ok(imageBytes);
        } catch (MalformedURLException | NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (IOException e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "Edit a pizza by given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Pizza successfully edited.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = PizzaResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Pizza with given id has not found.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "500", description = "Something happened saving the pizza's image.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @PutMapping("/{pizzaId}")
    public ResponseEntity<PizzaResponseDTO> editPizzaById(@PathVariable long pizzaId, @Valid PizzaEditRequestDTO pizza, @RequestParam(value = "file", required = false) MultipartFile image) {
        try {
            Pizza editedPizza = pizzaService.editPizzaById(pizzaId, pizza, image);

            return ResponseEntity.ok(PizzaResponseDTO.build(editedPizza));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Delete a pizza by given id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Pizza by given id successfully deleted.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "404", description = "Pizza has not found by given id.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER')")
    @DeleteMapping("/{pizzaId}")
    public ResponseEntity<Void> deletePizzaById(@PathVariable long pizzaId) {
        try {
            pizzaService.deletePizzaById(pizzaId);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
