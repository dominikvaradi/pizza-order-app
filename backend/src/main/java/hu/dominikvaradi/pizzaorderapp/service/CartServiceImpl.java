package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.Cart;
import hu.dominikvaradi.pizzaorderapp.data.model.CartItem;
import hu.dominikvaradi.pizzaorderapp.data.model.Pizza;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.cart.CartAddPizzaRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.repository.CartRepository;
import hu.dominikvaradi.pizzaorderapp.data.repository.PizzaRepository;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Service
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;

    private final PizzaRepository pizzaRepository;

    public CartServiceImpl(CartRepository cartRepository, PizzaRepository pizzaRepository) {
        this.cartRepository = cartRepository;
        this.pizzaRepository = pizzaRepository;
    }

    @Override
    public List<CartItem> getCartItemsByUserId(long userId) throws NotFoundException {
        Cart cart = getCartByUserId(userId);

        return cart.getItems();
    }

    @Override
    public void dumpAllCartItemsByUserId(long userId) throws NotFoundException {
        Cart cart = getCartByUserId(userId);

        cart.getItems().clear();

        cartRepository.save(cart);
    }

    @Override
    public void createNewCartItemByUserId(long userId, CartAddPizzaRequestDTO cartItemWithCountToAdd) throws NotFoundException {
        Cart cart = getCartByUserId(userId);

        Pizza pizzaToAdd = pizzaRepository.findById(cartItemWithCountToAdd.getPizzaId())
            .orElseThrow(() -> new NotFoundException("Pizza with given pizza id has not found! (pizzaId = " + cartItemWithCountToAdd.getPizzaId() + ")"));

        for(int i = 0; i < cartItemWithCountToAdd.getCount(); ++i) {
            cart.getItems().add(new CartItem(pizzaToAdd, cart));
        }

        cartRepository.save(cart);
    }

    @Override
    public void deleteCartItemByUserIdAndItemId(long userId, long cartItemId) throws NotFoundException {
        Cart cart = getCartByUserId(userId);

        cart.getItems().removeIf(cartItem -> cartItem.getId() == cartItemId);

        cartRepository.save(cart);
    }

    @Override
    public int getCartSizeByUserId(long userId) throws NotFoundException {
        Cart cart = getCartByUserId(userId);

        return cart.getItems().size();
    }

    private Cart getCartByUserId(long userId) throws NotFoundException {
        return cartRepository.findByUserId(userId)
            .orElseThrow(() -> new NotFoundException("Cart with given user id has not found! (userId = " + userId + ")"));
    }
}
