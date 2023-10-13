package mate.academy.bookshop.service.shoppingcart;

import lombok.RequiredArgsConstructor;
import mate.academy.bookshop.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookshop.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookshop.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookshop.exception.EntityNotFoundException;
import mate.academy.bookshop.mapper.CartItemMapper;
import mate.academy.bookshop.mapper.ShoppingCartMapper;
import mate.academy.bookshop.model.CartItem;
import mate.academy.bookshop.model.ShoppingCart;
import mate.academy.bookshop.model.User;
import mate.academy.bookshop.repository.cartitem.CartItemRepository;
import mate.academy.bookshop.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookshop.repository.user.UserRepository;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final CartItemRepository cartItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final CartItemMapper cartItemMapper;
    private final UserRepository userRepository;

    @Override
    public ShoppingCartDto getShoppingCartByUserId(Long id) {
        ShoppingCart shoppingCart = shoppingCartRepository.findShoppingCartByUserId(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find shoppingCart by id" + id));
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    public ShoppingCartDto addCartItemByUserId(Long id, CreateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCartById = getShoppingCartById(id);
        CartItem cartItem = cartItemMapper.toEntity(requestDto);
        cartItem.setShoppingCart(shoppingCartById);
        cartItemRepository.save(cartItem);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    @Override
    public ShoppingCartDto updateCartItem(Long id, Long cartItemId, CartItemUpdateDto updateDto) {
        CartItem cartItemFromDb = cartItemRepository.findById(cartItemId).orElseThrow(
                () -> new EntityNotFoundException("Can't find cartItem by id: " + cartItemId));
        CartItem cartItemDto = cartItemMapper.toEntity(updateDto);
        cartItemFromDb.setQuantity(cartItemDto.getQuantity());
        cartItemRepository.save(cartItemFromDb);
        return shoppingCartMapper.toDto(getShoppingCartById(id));
    }

    @Override
    public void deleteCartItem(Long userId, Long cartItemId) {
        cartItemRepository.deleteById(cartItemId);
        shoppingCartMapper.toDto(getShoppingCartById(userId));
    }

    private ShoppingCart getShoppingCartById(Long id) {
        User userFromDb = userRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Can't find user by id: " + id));
        return shoppingCartRepository.findShoppingCartByUserId(userFromDb.getId()).orElseThrow(
                () -> new EntityNotFoundException("Can't find shoppingCart by user id: " + id));
    }
}
