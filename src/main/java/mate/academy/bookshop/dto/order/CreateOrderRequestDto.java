package mate.academy.bookshop.dto.order;

import jakarta.validation.constraints.NotNull;
import java.util.Set;
import lombok.Data;
import mate.academy.bookshop.dto.orderitem.OrderItemDto;

@Data
public class CreateOrderRequestDto {
    @NotNull
    private Long userId;
    @NotNull
    private Set<OrderItemDto> orderItems;
    @NotNull
    private String shippingAddress;
}
