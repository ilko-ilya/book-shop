package mate.academy.bookshop.repository.orderitem;

import mate.academy.bookshop.model.OrderItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {
}
