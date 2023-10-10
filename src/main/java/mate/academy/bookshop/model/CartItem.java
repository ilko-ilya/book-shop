package mate.academy.bookshop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@AllArgsConstructor
@NoArgsConstructor
@SQLDelete(sql = "UPDATE cart_items SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Data
@Entity
@Table(name = "cart_items")
public class CartItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "shopping_cart_id", nullable = false)
    private ShoppingCart shoppingCart;
    @ManyToOne
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;
    @Column(nullable = false, name = "quantity")
    private int quantity;
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;
}
