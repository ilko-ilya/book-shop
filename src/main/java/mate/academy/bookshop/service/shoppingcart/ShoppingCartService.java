package mate.academy.bookshop.service.shoppingcart;

import mate.academy.bookshop.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookshop.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookshop.dto.shoppingcart.ShoppingCartDto;

public interface ShoppingCartService {
    ShoppingCartDto getShoppingCartByUserId(Long id);

    ShoppingCartDto addCartItemByUserId(Long id, CreateCartItemRequestDto requestDto);

    ShoppingCartDto updateCartItem(Long id, Long cartItemId, CartItemUpdateDto updateDto);

    void deleteCartItem(Long userId, Long cartItemId);
}
