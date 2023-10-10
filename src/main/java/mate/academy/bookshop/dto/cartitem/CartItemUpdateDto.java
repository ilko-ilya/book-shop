package mate.academy.bookshop.dto.cartitem;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CartItemUpdateDto {
    @NotNull
    private int quantity;
}
