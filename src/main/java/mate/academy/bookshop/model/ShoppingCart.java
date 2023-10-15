package mate.academy.bookshop.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MapsId;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE shopping_carts SET is_deleted = true WHERE id = ?")
@Where(clause = "is_deleted = false")
@Data
@Entity
@Table(name = "shopping_carts")
public class ShoppingCart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @MapsId
    private User user;
    @OneToMany(mappedBy = "shoppingCart")
    private Set<CartItem> cartItems;
    @Column(nullable = false, name = "is_deleted")
    private boolean isDeleted = false;
}
