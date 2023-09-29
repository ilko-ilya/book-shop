package mate.academy.bookshop.repository.role;

import java.util.Optional;
import mate.academy.bookshop.model.Role;
import mate.academy.bookshop.model.enums.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findRoleByName(RoleName roleName);
}
