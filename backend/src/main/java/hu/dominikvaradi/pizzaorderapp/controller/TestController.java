package hu.dominikvaradi.pizzaorderapp.controller;

import hu.dominikvaradi.pizzaorderapp.data.dto.user.UserRegisterRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.repository.OrderStatusRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.PaymentMethodRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.UserRepository;
import hu.dominikvaradi.pizzaorderapp.service.UserService;
import hu.dominikvaradi.pizzaorderapp.service.exception.ConflictException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private OrderStatusRepository orderStatusRepository;

    @Autowired
    private PaymentMethodRepository paymentMethodRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Transactional
    @GetMapping("/api/test")

    public ResponseEntity<Void> createRandomOrders() {
        for(int i = 0; i < 100; ++i) {
            UserRegisterRequestDTO userRegisterRequestDTO = new UserRegisterRequestDTO(
                    "TesztUser" + i,
                    "TesztJelszo1_" + i,
                    "tesztuser_teszt" + i + "@tesztmail.com",
                    "+36202653353",
                    "Teszt User " + i);
            try {
                userService.createUser(userRegisterRequestDTO, passwordEncoder);
            } catch (ConflictException e) {
                e.printStackTrace();
            }
        }

//        Optional<User> userOptional = userRepository.findByUsername("teszt1");
//
//        if (userOptional.isPresent()) {
//            User user = userOptional.get();
//
//            OrderStatus completedStatus = orderStatusRepository
//                    .findByName(EOrderStatus.STATUS_UNDER_PREPARATION)
//                    .orElse(new OrderStatus(EOrderStatus.STATUS_UNDER_PREPARATION));
//
//            PaymentMethod cashPaymentMethod = paymentMethodRepository
//                    .findByName(EPaymentMethod.PAYMENT_METHOD_CASH)
//                    .orElse(new PaymentMethod(EPaymentMethod.PAYMENT_METHOD_CASH));
//
//            for(int i = 0; i < 200; ++i) {
//                user.getOrders().add(new Order(LocalDateTime.now(), completedStatus, user, user.getAddresses().get(0), cashPaymentMethod));
//            }
//
//            userRepository.save(user);
//        }

        return ResponseEntity.ok().build();
    }
}
