package mate.academy.bookshop.service.order;

import java.util.List;
import mate.academy.bookshop.dto.order.OrderDto;
import mate.academy.bookshop.dto.orderitem.OrderItemDto;
import mate.academy.bookshop.model.enums.Status;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;

public interface OrderService {
    OrderDto createOrder(Authentication authentication);

    List<OrderDto> getUserOrderHistory(Long userId, Pageable pageable);

    void updateOrderStatus(Long orderId, Status status);

    List<OrderItemDto> getOrderItems(Long orderId);

    OrderItemDto findOrderItemById(Long orderId, Long itemId);
}
