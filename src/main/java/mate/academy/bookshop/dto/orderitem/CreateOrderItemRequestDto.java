package mate.academy.bookshop.dto.orderitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateOrderItemRequestDto {
    @NotNull
    private Long bookId;
    @NotNull
    @Positive
    private int quantity;
}
