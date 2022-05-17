package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.dto.cart.CartItemSaveRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.CartItem;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;

import java.util.List;

public interface CartService {
    List<CartItem> getCartItemsByUserId(long userId) throws NotFoundException;

    void dumpAllCartItemsByUserId(long userId) throws NotFoundException;

    void createNewCartItemByUserId(long userId, CartItemSaveRequestDTO cartItemToAdd) throws NotFoundException, BadRequestException;

    void deleteCartItemByUserIdAndItemId(long userid, long cartItemId) throws NotFoundException;

    int getCartSizeByUserId(long userId) throws NotFoundException;
}
