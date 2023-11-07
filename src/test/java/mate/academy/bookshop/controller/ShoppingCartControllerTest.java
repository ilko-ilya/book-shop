package mate.academy.bookshop.controller;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.HashSet;
import java.util.Set;
import mate.academy.bookshop.dto.BookDto;
import mate.academy.bookshop.dto.cartitem.CartItemUpdateDto;
import mate.academy.bookshop.dto.cartitem.CreateCartItemRequestDto;
import mate.academy.bookshop.model.Role;
import mate.academy.bookshop.model.User;
import mate.academy.bookshop.model.enums.RoleName;
import mate.academy.bookshop.service.book.BookService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ShoppingCartControllerTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private BookService bookService;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext applicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(applicationContext)
                .apply(SecurityMockMvcConfigurers.springSecurity())
                .build();
    }

    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    @DisplayName("Add new cartItem to the shoppingCart")
    @Sql(scripts =
            "classpath:database/cart-items/delete-cart_item-by-id-1-from-cart_items-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    @Test
    public void addCartItemToShoppingCart_ValidRequestDto_Success() throws Exception {
        User user = getMockUser(1L);

        BookDto savedBook = bookService.getBookById(1L);
        CreateCartItemRequestDto requestDto = new CreateCartItemRequestDto();
        requestDto.setBookId(savedBook.getId());
        requestDto.setQuantity(5);
        String jsonRequest = objectMapper.writeValueAsString(requestDto);
        mockMvc.perform(post("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(user))
                        .content(jsonRequest))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    @Test
    @DisplayName("Get shoppingCart")
    public void getShoppingCart_WithValidDate_Success() throws Exception {
        Long userId = 1L;
        User mockUser = getMockUser(userId);

        mockMvc.perform(get("/api/cart")
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(mockUser))
                )
                .andExpect(status().isOk());
    }

    @Test
    @DisplayName("Update shoppingCart")
    @Sql(scripts = "classpath:database/cart-items/insert-cart_item-to-cart_items-table.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/cart-items/delete-cart_item-from-cart_items-table.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    public void updateShoppingCart_WithValidDate_Success() throws Exception {
        Long userId = 1L;
        User mockUser = getMockUser(userId);

        Long cartItemId = 1L;
        CartItemUpdateDto updateDto = new CartItemUpdateDto();
        updateDto.setQuantity(3);

        mockMvc.perform(put("/api/cart/cart-items/{cartItemId}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(mockUser))
                        .content(objectMapper.writeValueAsString(updateDto)))
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "test", password = "test", roles = {"ADMIN", "USER"})
    @DisplayName("Delete cartItem by valid ID")
    @Test
    public void deleteCartItem_WithValidCartItemId_Success() throws Exception {
        Long cartItemId = 2L;
        Long userId = 2L;
        getMockUser(userId);

        mockMvc.perform(delete("/api/cart/cart-items/{cartItemId}", cartItemId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .with(user(getMockUser(userId))))
                .andExpect(status().isOk()
                );
    }

    private User getMockUser(Long userId) {
        User currentUser = new User();
        currentUser.setId(userId);

        Set<Role> roles = new HashSet<>();
        Role roleUser = new Role();
        roleUser.setName(RoleName.ROLE_USER);
        roles.add(roleUser);
        currentUser.setRoles(roles);

        return currentUser;
    }
}

