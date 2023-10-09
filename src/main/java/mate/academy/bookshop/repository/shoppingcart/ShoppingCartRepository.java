package mate.academy.bookshop.repository.shoppingcart;

import java.util.Optional;
import mate.academy.bookshop.model.ShoppingCart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCart, Long> {
    Optional<ShoppingCart> findShoppingCartByUserId(Long id);
}
