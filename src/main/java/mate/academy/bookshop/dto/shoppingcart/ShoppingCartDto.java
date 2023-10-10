package mate.academy.bookshop.dto.shoppingcart;

import java.util.Set;
import lombok.Data;
import mate.academy.bookshop.dto.cartitem.CartItemDto;

@Data
public class ShoppingCartDto {
    private Long id;
    private Long userId;
    private Set<CartItemDto> cartItems;
}
