package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.dto.order.OrderCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.order.OrderStatusEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.Order;
import hu.dominikvaradi.pizzaorderapp.data.model.OrderItem;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {
    Page<Order> getAllOrders(String term, Pageable pageable);

    Order createNewOrder(OrderCreateRequestDTO order) throws NotFoundException, BadRequestException;

    Page<Order> getAllActiveOrders(Pageable pageable);

    Order getOrderById(long orderId) throws NotFoundException;

    Order editOrderStatusById(long orderId, OrderStatusEditRequestDTO orderStatusEditRequest) throws BadRequestException, NotFoundException;

    List<OrderItem> getOrderItemsByOrderId(long orderId) throws NotFoundException;

    Page<Order> getAllOrdersByUserId(long userId, Pageable pageable) throws NotFoundException;

    Order getActiveOrderByUserId(long userId) throws NotFoundException;
}
