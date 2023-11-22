package mate.academy.bookshop.service;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookshop.dto.cartitem.CartItemDto;
import mate.academy.bookshop.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookshop.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookshop.dto.shoppingcart.ShoppingCartDto;
import mate.academy.bookshop.mapper.CartItemMapper;
import mate.academy.bookshop.mapper.ShoppingCartMapper;
import mate.academy.bookshop.model.Book;
import mate.academy.bookshop.model.CartItem;
import mate.academy.bookshop.model.ShoppingCart;
import mate.academy.bookshop.model.User;
import mate.academy.bookshop.repository.cartitem.CartItemRepository;
import mate.academy.bookshop.repository.shoppingcart.ShoppingCartRepository;
import mate.academy.bookshop.repository.user.UserRepository;
import mate.academy.bookshop.service.shoppingcart.ShoppingCartServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ShoppingCartServiceImplTest {
    @Mock
    private ShoppingCartRepository shoppingCartRepository;
    @Mock
    private CartItemMapper cartItemMapper;
    @Mock
    private ShoppingCartMapper shoppingCartMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private ShoppingCartServiceImpl shoppingCartService;
    private CartItem cartItem;

    @DisplayName("Verify return shoppingCart by valid UserID")
    @Test
    public void getShoppingCartByUserId_WithValidUserId_ShouldReturnShoppingCart() {
        User user = new User();
        user.setId(1L);

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);

        CartItemDto cartItemDto = new CartItemDto();
        cartItemDto.setId(cartItem.getId());

        ShoppingCart shoppingCart1 = new ShoppingCart();
        shoppingCart1.setId(1L);
        shoppingCart1.setUser(user);
        shoppingCart1.setCartItems(Set.of(cartItem));

        ShoppingCartDto expected = new ShoppingCartDto();
        expected.setId(shoppingCart1.getId());
        expected.setUserId(user.getId());
        expected.setCartItems(Set.of(cartItemDto));

        when(shoppingCartRepository.findShoppingCartByUserId(user.getId()))
                .thenReturn(Optional.of(shoppingCart1));
        when(shoppingCartMapper.toDto(shoppingCart1)).thenReturn(expected);

        ShoppingCartDto actual = shoppingCartService.getShoppingCartByUserId(1L);
        assertEquals(expected, actual);
    }

    @DisplayName("Verify that shoppingCart with such userID doesn't exist")
    @Test
    public void getShoppingCart_WithNonExistingUserId_ShouldReturnThrowException() {
        when(shoppingCartRepository.findShoppingCartByUserId(101L)).thenReturn(Optional.empty());
        Exception exception = assertThrows(RuntimeException.class,
                () -> shoppingCartService.getShoppingCartByUserId(101L));
        assertEquals("Can't find shoppingCart by userId: " + 101L, exception.getMessage());
    }

    @DisplayName("Verify that cartItem was added to ShoppingCart"
            + " with valid userID and return ShoppingCart")
    @Test
    public void addCartItemToShoppingCart_WithValidUserId_ShouldAddCartItem() {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(getCurrentUser());
        shoppingCart.setCartItems(new HashSet<>());
        shoppingCart.setId(1L);

        Book book = new Book();
        book.setId(1L);
        book.setAuthor("test");
        book.setCategories(new HashSet<>());
        book.setCoverImage("Cover Image");
        book.setDescription("description");
        book.setIsbn("isbn");
        book.setPrice(BigDecimal.TEN);
        book.setTitle("title");

        CartItem cartItem = new CartItem();
        cartItem.setId(1L);
        cartItem.setShoppingCart(shoppingCart);
        cartItem.setBook(book);

        when(cartItemMapper.toEntity(Mockito.<CreateCartItemRequestDto>any()))
                .thenReturn(cartItem);
        when(userRepository.findById(Mockito.<Long>any()))
                .thenReturn(Optional.of(getCurrentUser()));
        when(shoppingCartRepository.findShoppingCartByUserId(getCurrentUser().getId()))
                .thenReturn(Optional.of(shoppingCart));

        CreateCartItemRequestDto cartItemRequestDto = new CreateCartItemRequestDto();
        cartItemRequestDto.setBookId(1L);
        cartItemRequestDto.setQuantity(1);

        shoppingCartService.addCartItemByUserId(getCurrentUser().getId(), cartItemRequestDto);

        verify(cartItemMapper).toEntity(Mockito.<CreateCartItemRequestDto>any());
    }

    @DisplayName("Update a cartItem by ID and valid cartItemUpdateDto")
    @Test
    public void updateCartItem_WithValidParameters_ShouldUpdateCartItem() {
        Long cartItemId = 1L;
        ShoppingCart shoppingCart = createTestShoppingCartWithCartItem(cartItemId);

        CartItemUpdateDto cartItemUpdateDto = new CartItemUpdateDto();
        cartItemUpdateDto.setQuantity(3);

        CartItem updateCartItem = new CartItem();
        updateCartItem.setQuantity(cartItemUpdateDto.getQuantity());

        when(userRepository.findById(1L)).thenReturn(Optional.of(shoppingCart.getUser()));
        when(shoppingCartRepository.findShoppingCartByUserId(shoppingCart.getUser().getId()))
                .thenReturn(Optional.of(shoppingCart));
        when(cartItemRepository.findById(cartItemId)).thenReturn(Optional.of(cartItem));
        when(cartItemMapper.toEntity(cartItemUpdateDto)).thenReturn(updateCartItem);

        shoppingCartService.updateCartItem(1L, cartItem.getId(), cartItemUpdateDto);

        assertEquals(3, cartItem.getQuantity());
        verify(cartItemRepository, times(1)).save(any(CartItem.class));
        verify(cartItemMapper, times(1)).toEntity(cartItemUpdateDto);
        verify(userRepository, times(1)).findById(1L);
        verify(shoppingCartRepository, times(1))
                .findShoppingCartByUserId(shoppingCart.getUser().getId());
    }

    @DisplayName("Delete a cartItem by valid cartItem ID and user ID")
    @Test
    public void deleteCartItem_ValidCartItemIdAndUserId_ShouldDelete() {
        Long cartItemId = 1L;
        ShoppingCart shoppingCart = createTestShoppingCartWithCartItem(cartItemId);

        when(userRepository.findById(shoppingCart.getUser().getId()))
                .thenReturn(Optional.of(shoppingCart.getUser()));
        when(shoppingCartRepository.findShoppingCartByUserId(shoppingCart.getUser().getId()))
                .thenReturn(Optional.of(shoppingCart));
        shoppingCartService.deleteCartItem(shoppingCart.getUser().getId(), cartItemId);

        verify(userRepository, times(1)).findById(shoppingCart.getUser().getId());
        verify(shoppingCartRepository, times(1))
                .findShoppingCartByUserId(shoppingCart.getUser().getId());
    }

    private ShoppingCart createTestShoppingCartWithCartItem(Long cartItemId) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setId(1L);

        shoppingCart.setUser(getCurrentUser());

        cartItem = new CartItem();
        cartItem.setId(cartItemId);
        cartItem.setQuantity(2);

        Set<CartItem> cartItems = new HashSet<>();
        cartItems.add(cartItem);
        shoppingCart.setCartItems(cartItems);

        return shoppingCart;
    }

    private User getCurrentUser() {
        User currentUser = new User();
        currentUser.setId(1L);
        currentUser.setFirstName("Mila");
        currentUser.setLastName("Samilyak");
        currentUser.setEmail("s.mila@gmail.com");
        currentUser.setPassword("123456789");
        currentUser.setDeleted(true);
        currentUser.setRoles(new HashSet<>());
        currentUser.setShippingAddress("Gogol-ya 9");
        return currentUser;
    }
}
