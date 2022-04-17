package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.model.*;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.order.OrderCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.dto.order.OrderStatusEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.EOrderStatus;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.EPaymentMethod;
import hu.dominikvaradi.pizzaorderapp.data.repository.*;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final OrderStatusRepository orderStatusRepository;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, PaymentMethodRepository paymentMethodRepository, OrderStatusRepository orderStatusRepository, AddressRepository addressRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order createNewOrder(OrderCreateRequestDTO order) throws BadRequestException, NotFoundException {
        OrderStatus orderStatus_underProcess = orderStatusRepository.findByName(EOrderStatus.STATUS_UNDER_PROCESS)
            .orElse(new OrderStatus(EOrderStatus.STATUS_UNDER_PROCESS));

        Address address = addressRepository.findById(order.getAddressId())
            .orElseThrow(() -> new NotFoundException("Address with given id has not found! (addressId = " + order.getAddressId() + ")"));

        User user = userRepository.findById(order.getUserId())
            .orElseThrow(() -> new NotFoundException("User with given id has not found! (userId = " + order.getUserId() + ")"));

        PaymentMethod paymentMethod = paymentMethodRepository.findByName(EPaymentMethod.valueOf(order.getPaymentMethodName()))
            .orElseThrow(() -> new NotFoundException("Payment method with given name has not found! (paymentMethodName = " + order.getPaymentMethodName() + ")"));

        if (getUsersActiveOrder(order.getUserId()).isPresent()) {
            throw new BadRequestException("User already has an active order at the time!");
        }

        Order newOrder = new Order(LocalDateTime.now(), orderStatus_underProcess, user, address, paymentMethod);
        List<Pizza> orderedPizzasFromCart = user.getCart()
            .getItems()
            .stream()
            .map(CartItem::getPizza)
            .collect(Collectors.toList());

        List<OrderItem> orderItemList = newOrder.getItems();
        for(Pizza pizza : orderedPizzasFromCart) {
            orderItemList.add(new OrderItem(pizza, newOrder));
        }

        orderRepository.save(newOrder);

        return newOrder;
    }

    @Override
    public List<Order> getAllActiveOrders() {
        return orderRepository.findByOrderStatus_NameIsIn(getActiveOrderStatusList());
    }

    @Override
    public Order getOrderById(long orderId) throws NotFoundException {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Order with given id has not found! (orderId = " + orderId + ")"));
    }

    @Override
    public void editOrderStatusById(long orderId, OrderStatusEditRequestDTO orderStatusEditRequest) throws BadRequestException, NotFoundException {
        if (orderId != orderStatusEditRequest.getId()) {
            throw new BadRequestException("Path id is not the same as the order's id!");
        }

        OrderStatus newStatus = orderStatusRepository.findById(orderStatusEditRequest.getOrderStatusId())
            .orElseThrow(() -> new NotFoundException("Order status with given id has not found! (orderStatusId = " + orderStatusEditRequest.getOrderStatusId() + ")"));

        Order order = getOrderById(orderId);
        order.setOrderStatus(newStatus);

        orderRepository.save(order);
    }

    @Transactional
    @Override
    public List<Pizza> getPizzasByOrderId(long orderId) throws NotFoundException {
        Order order = getOrderById(orderId);

        return order.getItems()
            .stream()
            .map(OrderItem::getPizza)
            .collect(Collectors.toList());
    }

    @Override
    public List<Order> getAllOrdersByUserId(long userId) throws NotFoundException {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new NotFoundException("User with given id has not found! (userId = " + userId + ")"));

        return user.getOrders();
    }

    @Override
    public Order getActiveOrderByUserId(long userId) throws NotFoundException {
        return getUsersActiveOrder(userId)
            .orElseThrow(() -> new NotFoundException("Active order for given user id has not found! (userId = " + userId + ")"));
    }

    private Optional<Order> getUsersActiveOrder(long userId) {
        return orderRepository.findByUser_IdAndOrderStatus_NameIsIn(userId, getActiveOrderStatusList());
    }

    private List<EOrderStatus> getActiveOrderStatusList() {
        List<EOrderStatus> activeOrderStatusList = new ArrayList<>();
        activeOrderStatusList.add(EOrderStatus.STATUS_UNDER_PROCESS);
        activeOrderStatusList.add(EOrderStatus.STATUS_UNDER_DELIVER);
        activeOrderStatusList.add(EOrderStatus.STATUS_UNDER_PREPARATION);

        return activeOrderStatusList;
    }
}
