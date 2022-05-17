package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.dto.enums.OrderStatusResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.enums.PaymentMethodDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.enums.RoleResponseDTO;
import hu.dominikvaradi.pizzaorderapp.data.repository.OrderStatusRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.PaymentMethodRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.RoleRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@CrossOrigin({"http://192.168.1.10:4200", "http://localhost:4200"})
@RequestMapping("/api/enums")
@RestController
public class EnumController {
    private final RoleRepository roleRepository;

    private final OrderStatusRepository orderStatusRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    public EnumController(RoleRepository roleRepository, OrderStatusRepository orderStatusRepository, PaymentMethodRepository paymentMethodRepository) {
        this.roleRepository = roleRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.paymentMethodRepository = paymentMethodRepository;
    }

    @GetMapping("/roles")
    public ResponseEntity<List<RoleResponseDTO>> getRoles() {
        return ResponseEntity.ok(roleRepository
                .findAll()
                .stream()
                .map(RoleResponseDTO::build)
                .collect(Collectors.toList()));
    }

    @GetMapping("/order-statuses")
    public ResponseEntity<List<OrderStatusResponseDTO>> getOrderStatuses() {
        return ResponseEntity.ok(orderStatusRepository
                .findAll()
                .stream()
                .map(OrderStatusResponseDTO::build)
                .collect(Collectors.toList()));
    }

    @GetMapping("/payment-methods")
    public ResponseEntity<List<PaymentMethodDTO>> getPaymentMethods() {
        return ResponseEntity.ok(paymentMethodRepository
                .findAll()
                .stream()
                .map(PaymentMethodDTO::build)
                .collect(Collectors.toList()));
    }
}
