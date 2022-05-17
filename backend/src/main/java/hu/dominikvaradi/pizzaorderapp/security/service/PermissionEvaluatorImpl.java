package hu.dominikvaradi.pizzaorderapp.security.service;

import hu.dominikvaradi.pizzaorderapp.data.model.User;
import hu.dominikvaradi.pizzaorderapp.service.AddressService;
import hu.dominikvaradi.pizzaorderapp.service.OrderService;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.io.Serializable;

@Component
public class PermissionEvaluatorImpl implements PermissionEvaluator {
    @Autowired
    private OrderService orderService;

    @Autowired
    private AddressService addressService;

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        User user = ((UserDetailsImpl) authentication.getPrincipal()).getUser();
        String permissionString = (String) permission;

        if (permissionString.equals("address")) {
            long addressId = (long) targetDomainObject;

            try {
                return addressService.getAddressById(addressId).getUser().getId() == user.getId();
            } catch (NotFoundException e) {
                return false;
            }
        } else if (permissionString.equals("order")) {
            long orderId = (long) targetDomainObject;

            try {
                return orderService.getOrderById(orderId).getUser().getId() == user.getId();
            } catch (NotFoundException e) {
                return false;
            }
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        return false;
    }
}
