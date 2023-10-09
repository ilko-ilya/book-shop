package mate.academy.bookshop.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

@Data
public class CreateCartItemRequestDto {
    @NotNull
    private Long bookId;
    @NotNull
    @Positive
    private int quantity;
}
