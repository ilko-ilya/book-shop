package mate.academy.bookshop.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.order.OrderDto;
import mate.academy.bookshop.dto.order.OrderItemDto;
import mate.academy.bookshop.model.User;
import mate.academy.bookshop.model.enums.Status;
import mate.academy.bookshop.service.order.OrderService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RequiredArgsConstructor
@RestController
@RequestMapping(name = "/api/orders")
public class OrderController {
    private final OrderService orderService;

    @Operation(summary = "Create an order", description = "Create an order")
    @PreAuthorize("hasRole('ROLE_USER')")
    @PostMapping
    public OrderDto save(Authentication authentication) {
        return orderService.createOrder(authentication);
    }

    @Operation(summary = "Get history of all orders",
            description = "Get history of all orders")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping
    public List<OrderDto> getAllHistoryOrders(
            Authentication authentication,
            Pageable pageable) {
        User user = (User) authentication.getPrincipal();
        return orderService.getUserOrderHistory(user.getId(), pageable);
    }

    @Operation(summary = "Get All orderItems by orderId",
            description = "Get all orderItems by orderId")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getAllOrderItemsByOrderId(@PathVariable Long orderId) {
        return orderService.getOrderItems(orderId);
    }

    @Operation(summary = "Get orderItem by orderId and by itemId",
            description = "Get orderItem by id and by itemId")
    @PreAuthorize("hasRole('ROLE_USER')")
    @GetMapping("/{orderId}/items/{orderItemId}")
    public OrderItemDto getOrderItemById(
            @PathVariable Long orderId,
            @PathVariable Long orderItemId) {
        return orderService.findOrderItemById(orderId, orderItemId);
    }

    @Operation(summary = "Update order status", description = "Update order status")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PatchMapping("/{id}")
    public void updateOrderStatus(@PathVariable Long id, @RequestBody Status status) {
        orderService.updateOrderStatus(id, status);
    }
}
