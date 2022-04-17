package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.model.Pizza;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.MessageResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.pizza.PizzaResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.pizza.PizzaSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.service.PizzaService;
import hu.dominikvaradi.pizzaorderapp.service.PizzaServiceImpl;
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
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class PizzaController {
    private final PizzaService pizzaService;

    public PizzaController(PizzaService pizzaService) {
        this.pizzaService = pizzaService;
    }

    @Operation(summary = "Get all pizzas.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All pizzas.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PizzaResponseDTO[].class)))
        })
    })
    @GetMapping("/api/pizzas")
    public ResponseEntity<?> getAllPizzas() {
        List<Pizza> pizzas = pizzaService.getAllPizzas();

        return ResponseEntity.ok(pizzas
            .stream()
            .map(PizzaResponseDTO::build)
            .collect(Collectors.toList()));
    }

    @Operation(summary = "Create a new pizza.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Pizza successfully created.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PizzaResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "500", description = "Something happened saving the pizza's image.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @PostMapping("/api/pizzas")
    public ResponseEntity<?> createNewPizza(@Valid @ModelAttribute PizzaSaveRequestDTO pizza) {
        try {
            Pizza newPizza = pizzaService.createNewPizza(pizza);

            return ResponseEntity.created(URI.create("/api/pizza/" + newPizza.getId()))
                .body(PizzaResponseDTO.build(newPizza));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponseDTO(e.getMessage()));
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError()
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Get a pizza by given id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pizza by given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PizzaResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "404", description = "Pizza has not found by given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @GetMapping("/api/pizza/{id}")
    public ResponseEntity<?> getPizzaById(@PathVariable long id) {
        try {
            Pizza pizza = pizzaService.getPizzaById(id);

            return ResponseEntity.ok(PizzaResponseDTO.build(pizza));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Get a pizza's image by given file name.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Pizza's image.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = byte[].class)))
        }),
        @ApiResponse(responseCode = "404", description = "Pizza image has not found by given file name.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "500", description = "Something happened getting the pizza's image.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @ResponseBody
    @GetMapping(value = "/api/pizza-images/{fileName}", produces = "image/*")
    public ResponseEntity<?> getPizzaImageByFileName(@PathVariable String fileName) {
        try {
            Path pizzaImagePath = PizzaServiceImpl.pizzaImagesRoot.resolve(fileName);
            Resource pizzaImageResource = new UrlResource(pizzaImagePath.toUri());

            return ResponseEntity.ok(pizzaImageResource.getInputStream().readAllBytes());
        } catch (MalformedURLException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        } catch (IOException e) {
            return ResponseEntity.internalServerError()
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Edit a pizza by given id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pizza successfully edited."),
        @ApiResponse(responseCode = "404", description = "Pizza with given id has not found.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "500", description = "Something happened saving the pizza's image.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @PutMapping("/api/pizza/{id}")
    public ResponseEntity<?> editPizzaById(@PathVariable long id, @Valid @ModelAttribute PizzaSaveRequestDTO pizza) {
        try {
            pizzaService.editPizzaById(id, pizza);

            return ResponseEntity.noContent().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponseDTO(e.getMessage()));
        } catch (InternalServerErrorException e) {
            return ResponseEntity.internalServerError()
                .body(new MessageResponseDTO(e.getMessage()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Delete a pizza by given id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Pizza by given id successfully deleted."),
        @ApiResponse(responseCode = "404", description = "Pizza has not found by given id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @DeleteMapping("/api/pizza/{id}")
    public ResponseEntity<?> deletePizzaById(@PathVariable long id) {
        try {
            pizzaService.deletePizzaById(id);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }
}
