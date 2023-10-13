package mate.academy.bookshop.dto.order;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Set;
import lombok.Data;
import mate.academy.bookshop.dto.orderitem.OrderItemDto;
import mate.academy.bookshop.model.enums.Status;

@Data
public class OrderDto {
    private Long id;
    private Long userId;
    private Set<OrderItemDto> orderItems;
    private LocalDateTime orderDate;
    private BigDecimal total;
    private Status status;
    private String shippingAddress;
}
