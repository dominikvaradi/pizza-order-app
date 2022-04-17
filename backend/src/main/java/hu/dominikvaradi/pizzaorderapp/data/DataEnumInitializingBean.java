package hu.dominikvaradi.pizzaorderapp.data;

import hu.dominikvaradi.pizzaorderapp.data.model.OrderStatus;
import hu.dominikvaradi.pizzaorderapp.data.model.PaymentMethod;
import hu.dominikvaradi.pizzaorderapp.data.model.Role;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.EOrderStatus;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.EPaymentMethod;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.ERole;
import hu.dominikvaradi.pizzaorderapp.data.repository.OrderStatusRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.PaymentMethodRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.RoleRepository;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

@Component
public class DataEnumInitializingBean implements InitializingBean {
    private final OrderStatusRepository orderStatusRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final RoleRepository roleRepository;

    public DataEnumInitializingBean(OrderStatusRepository orderStatusRepository, PaymentMethodRepository paymentMethodRepository, RoleRepository roleRepository) {
        this.orderStatusRepository = orderStatusRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public void afterPropertiesSet() {
        for(EOrderStatus orderStatusName : EOrderStatus.values()) {
            OrderStatus orderStatus = orderStatusRepository.findByName(orderStatusName).orElse(new OrderStatus(orderStatusName));
            orderStatusRepository.save(orderStatus);
        }

        for(EPaymentMethod paymentMethodName : EPaymentMethod.values()) {
            PaymentMethod paymentMethod = paymentMethodRepository.findByName(paymentMethodName).orElse(new PaymentMethod(paymentMethodName));
            paymentMethodRepository.save(paymentMethod);
        }

        for(ERole roleName : ERole.values()) {
            Role role = roleRepository.findByName(roleName).orElse(new Role(roleName));
            roleRepository.save(role);
        }
    }
}
