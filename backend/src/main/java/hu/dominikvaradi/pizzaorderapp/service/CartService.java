package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.dto.cart.CartAddPizzaRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.CartItem;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;

import java.util.List;

public interface CartService {
    List<CartItem> getCartItemsByUserId(long userId) throws NotFoundException;

    void dumpAllCartItemsByUserId(long userId) throws NotFoundException;

    void createNewCartItemByUserId(long userid, CartAddPizzaRequestDTO cartItemWithCountToAdd) throws NotFoundException;

    void deleteCartItemByUserIdAndItemId(long userid, long cartItemId) throws NotFoundException;

    int getCartSizeByUserId(long userId) throws NotFoundException;
}
