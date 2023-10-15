package mate.academy.bookshop.service.order;

import jakarta.transaction.Transactional;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.order.OrderDto;
import mate.academy.bookshop.dto.order.OrderItemDto;
import mate.academy.bookshop.exception.EntityNotFoundException;
import mate.academy.bookshop.mapper.OrderItemMapper;
import mate.academy.bookshop.mapper.OrderMapper;
import mate.academy.bookshop.model.CartItem;
import mate.academy.bookshop.model.Order;
import mate.academy.bookshop.model.OrderItem;
import mate.academy.bookshop.model.ShoppingCart;
import mate.academy.bookshop.model.User;
import mate.academy.bookshop.model.enums.Status;
import mate.academy.bookshop.repository.cartitem.CartItemRepository;
import mate.academy.bookshop.repository.order.OrderItemRepository;
import mate.academy.bookshop.repository.order.OrderRepository;
import mate.academy.bookshop.repository.shoppingcart.ShoppingCartRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemRepository orderItemRepository;
    private final OrderItemMapper orderItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    public OrderDto createOrder(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        ShoppingCart shoppingCartByUserId = shoppingCartRepository
                .findShoppingCartByUserId(user.getId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Can't find shoppingCart by userId: " + user.getId()));
        Order order = buildOrder(user);
        Set<OrderItem> orderItemsFromShoppingCart = getOrderItemsFromShoppingCart(
                shoppingCartByUserId, order);
        BigDecimal total = calculateTotal(orderItemsFromShoppingCart);
        order.setTotal(total);

        clearShoppingCart(shoppingCartByUserId);

        orderRepository.save(order);
        order.setOrderItems(orderItemsFromShoppingCart);
        orderItemRepository.saveAll(orderItemsFromShoppingCart);
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public List<OrderDto> getUserOrderHistory(Long userId, Pageable pageable) {
        return orderRepository.findAllByUserId(userId, pageable)
                .stream()
                .map(orderMapper::toDto)
                .toList();
    }

    @Override
    @Transactional
    public void updateOrderStatus(Long orderId, Status status) {
        orderRepository.updateStatus(orderId, status);
    }

    @Override
    public List<OrderItemDto> getOrderItems(Long orderId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by orderId: " + orderId));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    public OrderItemDto findOrderItemById(Long orderId, Long itemId) {
        Order order = orderRepository.findById(orderId).orElseThrow(
                () -> new EntityNotFoundException("Can't find order by orderId: " + orderId));
        OrderItem orderItem = order.getOrderItems()
                .stream()
                .filter(item -> item.getId().equals(itemId))
                .findFirst()
                .orElseThrow(() -> new EntityNotFoundException(
                        "OrderItem not found with id: " + itemId));
        return orderItemMapper.toDto(orderItem);
    }

    private OrderItem createOrderItem(CartItem cartItem, Order order) {
        OrderItem orderItem = new OrderItem();
        orderItem.setBook(cartItem.getBook());
        orderItem.setOrder(order);
        orderItem.setPrice(cartItem.getBook().getPrice());
        orderItem.setQuantity(cartItem.getQuantity());
        return orderItem;
    }

    private Set<OrderItem> getOrderItemsFromShoppingCart(ShoppingCart shoppingCart, Order order) {
        return shoppingCart.getCartItems()
                .stream()
                .map(cartItem -> createOrderItem(cartItem, order))
                .collect(Collectors.toSet());
    }

    private Order buildOrder(User user) {
        Order order = new Order();
        order.setOrderDate(LocalDateTime.now());
        order.setUser(user);
        order.setStatus(Status.PENDING);
        order.setShippingAddress(user.getShippingAddress());
        return order;
    }

    private BigDecimal calculateTotal(Set<OrderItem> orderItems) {
        return orderItems.stream()
                .map(orderItem -> orderItem.getBook().getPrice()
                        .multiply(new BigDecimal(orderItem.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private void clearShoppingCart(ShoppingCart shoppingCart) {
        shoppingCart.getCartItems().forEach(
                cartItem -> cartItemRepository.deleteById(cartItem.getId()));
    }
}
