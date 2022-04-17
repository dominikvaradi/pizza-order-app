package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.Order;
import hu.dominikvaradi.pizzaorderapp.data.model.Pizza;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.order.OrderCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.order.OrderStatusEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;

import java.util.List;

public interface OrderService {
    List<Order> getAllOrders();

    Order createNewOrder(OrderCreateRequestDTO order) throws NotFoundException, BadRequestException;

    List<Order> getAllActiveOrders();

    Order getOrderById(long orderId) throws NotFoundException;

    void editOrderStatusById(long orderId, OrderStatusEditRequestDTO orderStatusEditRequest) throws BadRequestException, NotFoundException;

    List<Pizza> getPizzasByOrderId(long orderId) throws NotFoundException;

    List<Order> getAllOrdersByUserId(long userId) throws NotFoundException;

    Order getActiveOrderByUserId(long userId) throws NotFoundException;
}
