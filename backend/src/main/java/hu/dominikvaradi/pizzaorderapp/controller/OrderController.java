package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.model.Order;
import hu.dominikvaradi.pizzaorderapp.data.model.Pizza;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.MessageResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.order.OrderCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.order.OrderResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.order.OrderStatusEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.order.OrderWithPizzasResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.pizza.PizzaResponseDTO;
import hu.dominikvaradi.pizzaorderapp.service.OrderService;
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
import java.io.Serializable;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Get all orders, optionally with their items (pizzas).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All orders.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO[].class)))
        }),
        @ApiResponse(responseCode = "200", description = "All orders with their items (pizzas).", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderWithPizzasResponseDTO[].class)))
        })
    })
    @GetMapping("/api/orders")
    public ResponseEntity<?> getAllOrders(@RequestParam(defaultValue = "false") boolean withItems) {
        List<Order> orders = orderService.getAllOrders();

        return getResponseByOptionalItems(withItems, orders);
    }

    @Operation(summary = "Create a new order.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Order successfully created.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "404", description = "Given user, address, or payment method not found.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Provided request is invalid or the given user already has an active order at the time.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @PostMapping("/api/orders")
    public ResponseEntity<?> createNewOrder(@Valid @RequestBody OrderCreateRequestDTO order) {
        try {
            Order newOrder = orderService.createNewOrder(order);

            return ResponseEntity.created(URI.create("/api/order/" + newOrder.getId()))
                .body(OrderResponseDTO.build(newOrder));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponseDTO(e.getMessage()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Get all active orders, optionally with their items (pizzas).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "All active orders.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO[].class)))
        }),
        @ApiResponse(responseCode = "200", description = "All active orders with their items (pizzas).", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderWithPizzasResponseDTO[].class)))
        })
    })
    @GetMapping("/api/orders/active")
    public ResponseEntity<?> getAllActiveOrders(@RequestParam(defaultValue = "false") boolean withItems) {
        List<Order> orders = orderService.getAllActiveOrders();

        return getResponseByOptionalItems(withItems, orders);
    }

    @Operation(summary = "Get order by order id, optionally with it's items (pizzas).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order by given order id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "200", description = "Order by given order id with it's items (pizzas).", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderWithPizzasResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "404", description = "Order by given order id has not found.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @GetMapping("/api/order/{orderId}")
    public ResponseEntity<?> getOrderById(@RequestParam(defaultValue = "false") boolean withItems, @PathVariable long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);

            return ResponseEntity.ok(withItems ? OrderWithPizzasResponseDTO.build(order) : OrderResponseDTO.build(order));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Edit order's status by order id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Order's status successfully edited."),
        @ApiResponse(responseCode = "404", description = "Order with given id or order status with given id has not found.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @PutMapping("/api/order/{orderId}")
    public ResponseEntity<?> editOrderStatusById(@PathVariable long orderId, @Valid @RequestBody OrderStatusEditRequestDTO orderStatusEditRequest) {
        try {
            orderService.editOrderStatusById(orderId, orderStatusEditRequest);

            return ResponseEntity.noContent().build();
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest()
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Get an order's items (pizzas) by given order id.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Order's items (pizzas) by given order id.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = PizzaResponseDTO[].class)))
        }),
        @ApiResponse(responseCode = "404", description = "Order with given id has not found.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @GetMapping("/api/order/{orderId}/items")
    public ResponseEntity<?> getOrderItemsByOrderId(@PathVariable long orderId) {
        try {
            List<Pizza> items = orderService.getPizzasByOrderId(orderId);

            return ResponseEntity.ok(items.stream()
                .map(pizza -> new PizzaResponseDTO(pizza.getId(),
                    pizza.getName(),
                    pizza.getDescription(),
                    pizza.getPrice(),
                    pizza.getImageName()))
                .collect(Collectors.toList()));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Get a user's orders by given user id, optionally with their items (pizzas).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User's orders.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO[].class)))
        }),
        @ApiResponse(responseCode = "200", description = "User's order with their items.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderWithPizzasResponseDTO[].class)))
        }),
        @ApiResponse(responseCode = "404", description = "User with given user id has not found.", content = {
            @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @GetMapping("/api/user/{userId}/orders")
    public ResponseEntity<?> getAllOrdersByUserId(@RequestParam(defaultValue = "false") boolean withItems, @PathVariable long userId) {
        try {
            List<Order> orders = orderService.getAllOrdersByUserId(userId);

            return getResponseByOptionalItems(withItems, orders);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    @Operation(summary = "Get a user's active order by given user id, optionally with it's items (pizzas).")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "User's active order", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "200", description = "User's active order with it's items.", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderWithPizzasResponseDTO.class)))
        }),
        @ApiResponse(responseCode = "404", description = "User with given user id has not found.", content = {
                @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = MessageResponseDTO.class)))
        })
    })
    @GetMapping("/api/user/{userId}/orders/active")
    public ResponseEntity<?> getActiveOrderByUserId(@RequestParam(defaultValue = "false") boolean withItems, @PathVariable long userId) {
        try {
            Order activeOrder = orderService.getActiveOrderByUserId(userId);

            return ResponseEntity.ok(withItems ? OrderWithPizzasResponseDTO.build(activeOrder) : OrderResponseDTO.build(activeOrder));
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(new MessageResponseDTO(e.getMessage()));
        }
    }

    private ResponseEntity<? extends List<? extends Serializable>> getResponseByOptionalItems(boolean withItems, List<Order> orders) {
        return ResponseEntity.ok(orders
            .stream()
            .map(order -> {
                if (withItems) {
                    return OrderWithPizzasResponseDTO.build(order);
                } else {
                    return OrderResponseDTO.build(order);
                }
            })
            .collect(Collectors.toList()));
    }
}
