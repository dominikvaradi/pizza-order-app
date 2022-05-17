package hu.dominikvaradi.pizzaorderapp.service;

import hu.dominikvaradi.pizzaorderapp.data.dto.order.OrderCreateRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.dto.order.OrderStatusEditRequestDTO;
import hu.dominikvaradi.pizzaorderapp.data.model.*;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.EOrderStatus;
import hu.dominikvaradi.pizzaorderapp.data.model.enums.EPaymentMethod;
import hu.dominikvaradi.pizzaorderapp.data.repository.*;
import hu.dominikvaradi.pizzaorderapp.service.exception.BadRequestException;
import hu.dominikvaradi.pizzaorderapp.service.exception.NotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Transactional
@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;

    private final PaymentMethodRepository paymentMethodRepository;

    private final OrderStatusRepository orderStatusRepository;

    private final AddressRepository addressRepository;

    private final UserRepository userRepository;

    private final CartRepository cartRepository;

    public OrderServiceImpl(OrderRepository orderRepository, PaymentMethodRepository paymentMethodRepository, OrderStatusRepository orderStatusRepository, AddressRepository addressRepository, UserRepository userRepository, CartRepository cartRepository) {
        this.orderRepository = orderRepository;
        this.paymentMethodRepository = paymentMethodRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.addressRepository = addressRepository;
        this.userRepository = userRepository;
        this.cartRepository = cartRepository;
    }

    @Override
    public Page<Order> getAllOrders(String term, Pageable pageable) {
        if (term == null || term.isBlank()) {
            return orderRepository.findAll(pageable);
        }

        return orderRepository.findByUserUsernameOrUserFullNameContainsAllIgnoreCase(term, pageable);
    }

    @Override
    public Order createNewOrder(OrderCreateRequestDTO order) throws BadRequestException, NotFoundException {
        OrderStatus orderStatusUnderProcess = orderStatusRepository.findByName(EOrderStatus.STATUS_UNDER_PROCESS)
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

        Order newOrder = new Order(LocalDateTime.now(), orderStatusUnderProcess, user, address, paymentMethod);
        Cart cartOfUser = cartRepository.findByUserId(user.getId())
            .orElseThrow(() -> new NotFoundException("User's cart with given user id has not found! (userId = " + order.getUserId() + ")"));

        for(CartItem cartItem : cartOfUser.getItems()) {
            OrderItem newOrderItem = new OrderItem(cartItem.getPizza(), newOrder, cartItem.getAmount());
            newOrder.getItems().add(newOrderItem);
        }

        cartOfUser.getItems().clear();

        orderRepository.save(newOrder);
        cartRepository.save(cartOfUser);

        return newOrder;
    }

    @Override
    public Page<Order> getAllActiveOrders(Pageable pageable) {
        return orderRepository.findByOrderStatusNameIsIn(getActiveOrderStatusList(), pageable);
    }

    @Override
    public Order getOrderById(long orderId) throws NotFoundException {
        return orderRepository.findById(orderId)
            .orElseThrow(() -> new NotFoundException("Order with given id has not found! (orderId = " + orderId + ")"));
    }

    @Override
    public Order editOrderStatusById(long orderId, OrderStatusEditRequestDTO orderStatusEditRequest) throws BadRequestException, NotFoundException {
        if (orderId != orderStatusEditRequest.getId()) {
            throw new BadRequestException("Path id is not the same as the order's id!");
        }

        OrderStatus newStatus = orderStatusRepository.findById(orderStatusEditRequest.getOrderStatusId())
            .orElseThrow(() -> new NotFoundException("Order status with given id has not found! (orderStatusId = " + orderStatusEditRequest.getOrderStatusId() + ")"));

        Order order = getOrderById(orderId);
        order.setOrderStatus(newStatus);

        return orderRepository.save(order);
    }

    @Transactional
    @Override
    public List<OrderItem> getOrderItemsByOrderId(long orderId) throws NotFoundException {
        Order order = getOrderById(orderId);

        return order.getItems();
    }

    @Override
    public Page<Order> getAllOrdersByUserId(long userId, Pageable pageable) throws NotFoundException {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException("User with given id has not found! (userId = " + userId + ")");
        }

        return orderRepository.findByUserId(userId, pageable);
    }

    @Override
    public Order getActiveOrderByUserId(long userId) throws NotFoundException {
        return getUsersActiveOrder(userId)
            .orElseThrow(() -> new NotFoundException("Active order for given user id has not found! (userId = " + userId + ")"));
    }

    private Optional<Order> getUsersActiveOrder(long userId) {
        return orderRepository.findByUserIdAndOrderStatusNameIsIn(userId, getActiveOrderStatusList());
    }

    private List<EOrderStatus> getActiveOrderStatusList() {
        List<EOrderStatus> activeOrderStatusList = new ArrayList<>();
        activeOrderStatusList.add(EOrderStatus.STATUS_UNDER_PROCESS);
        activeOrderStatusList.add(EOrderStatus.STATUS_UNDER_DELIVER);
        activeOrderStatusList.add(EOrderStatus.STATUS_UNDER_PREPARATION);

        return activeOrderStatusList;
    }
}
