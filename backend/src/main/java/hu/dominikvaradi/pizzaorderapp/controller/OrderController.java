package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.dto.order.OrderCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.order.OrderItemResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.order.OrderResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.order.OrderStatusEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Order;
import hu.dominikvaradi.pizzaorderapp.data.model.OrderItem;
import hu.dominikvaradi.pizzaorderapp.service.OrderService;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
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
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin({"http://192.168.1.10:4200", "http://localhost:4200"})
@RequestMapping("/api/orders")
@RestController
public class OrderController {
    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @Operation(summary = "Get all orders, optionally with their items (pizzas) or only the active ones.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order list.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class)))
            }),
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER') or (hasRole('ROLE_CHEF') and #activeOnly)")
    @Transactional
    @GetMapping
    public ResponseEntity<Page<OrderResponseDTO>> getAllOrders(
            @RequestParam(defaultValue = "false") boolean withItems,
            @RequestParam(defaultValue = "false") boolean activeOnly,
            @RequestParam(required = false) String term,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size
    ) {
        Pageable pageable = PageRequest.of(page, size);

        Page<Order> orderPage;
        if (activeOnly) {
            orderPage = orderService.getAllActiveOrders(pageable);
        } else {
            orderPage = orderService.getAllOrders(term, pageable);
        }

        return ResponseEntity.ok(orderPage.map(order -> OrderResponseDTO.build(order, withItems)));
    }

    @Operation(summary = "Create a new order.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Order successfully created.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Given user, address, or payment method not found.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "400", description = "Provided request is invalid or the given user already has an active order at the time.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PostMapping("")
    public ResponseEntity<OrderResponseDTO> createNewOrder(@Valid @RequestBody OrderCreateRequestDTO order) {
        try {
            Order newOrder = orderService.createNewOrder(order);

            return ResponseEntity.created(URI.create("/api/order/" + newOrder.getId()))
                .body(OrderResponseDTO.build(newOrder, false));
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Get order by order id, optionally with it's items (pizzas).")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order by given order id.", content = {
                    @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class))
            }),
            @ApiResponse(responseCode = "404", description = "Order by given order id has not found.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER') or hasPermission(#orderId, 'order')")
    @Transactional
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@RequestParam(defaultValue = "false") boolean withItems, @PathVariable long orderId) {
        try {
            Order order = orderService.getOrderById(orderId);

            return ResponseEntity.ok(OrderResponseDTO.build(order, withItems));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @Operation(summary = "Edit order's status by order id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Order's status successfully edited.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "404", description = "Order with given id or order status with given id has not found.", content = {
                    @Content(schema = @Schema(hidden = true))
            }),
            @ApiResponse(responseCode = "400", description = "Provided request is invalid.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER', 'ROLE_CHEF')")
    @Transactional
    @PutMapping("/{orderId}")
    public ResponseEntity<OrderResponseDTO> editOrderStatusById(@PathVariable long orderId, @Valid @RequestBody OrderStatusEditRequestDTO orderStatusEditRequest) {
        try {
            Order order = orderService.editOrderStatusById(orderId, orderStatusEditRequest);

            return ResponseEntity.ok(OrderResponseDTO.build(order, true));
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (BadRequestException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @Operation(summary = "Get an order's items (pizzas) by given order id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Order's items (pizzas) by given order id.", content = {
                    @Content(mediaType = "application/json", array = @ArraySchema(schema = @Schema(implementation = OrderItemResponseDTO.class)))
            }),
            @ApiResponse(responseCode = "404", description = "Order with given id has not found.", content = {
                    @Content(schema = @Schema(hidden = true))
            })
    })
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_MANAGER') or hasPermission(#orderId, 'order')")
    @GetMapping("/{orderId}/items")
    public ResponseEntity<List<OrderItemResponseDTO>> getOrderItemsByOrderId(@PathVariable long orderId) {
        try {
            List<OrderItem> items = orderService.getOrderItemsByOrderId(orderId);

            return ResponseEntity.ok(
                items.stream()
                .map(OrderItemResponseDTO::build)
                .collect(Collectors.toList())
            );
        } catch (NotFoundException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
