package mate.academy.bookshop.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.bookshop.model.ShoppingCart;
import mate.academy.bookshop.model.User;
import mate.academy.bookshop.repository.shoppingcart.ShoppingCartRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class ShoppingCartRepositoryTest {
    @Autowired
    private ShoppingCartRepository shoppingCartRepository;

    @Test
    @DisplayName("Find a shoppingCart by user ID")
    public void findShoppingCartByUserId() {
        User user = new User();
        user.setId(1L);
        user.setFirstName("Mila");
        user.setEmail("mila@gmail.com");

        Long userId = user.getId();
        Optional<ShoppingCart> foundShoppingCart =
                shoppingCartRepository.findShoppingCartByUserId(userId);

        assertTrue(foundShoppingCart.isPresent());

        ShoppingCart retrievedShoppingCart = foundShoppingCart.get();
        assertEquals(user.getId(), retrievedShoppingCart.getUser().getId());
        assertEquals(user.getUsername(), retrievedShoppingCart.getUser().getUsername());
        assertEquals(user.getEmail(), retrievedShoppingCart.getUser().getEmail());
    }
}
