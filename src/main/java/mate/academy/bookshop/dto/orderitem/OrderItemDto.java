package mate.academy.bookshop.dto.orderitem;

import lombok.Data;

@Data
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
