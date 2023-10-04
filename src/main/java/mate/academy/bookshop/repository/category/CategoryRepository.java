package mate.academy.bookshop.repository.category;

import java.util.Set;
import mate.academy.bookshop.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepository extends JpaRepository<Category, Long> {
    Set<Category> findCategoriesByIdIn(Set<Long> categoryIds);

}
