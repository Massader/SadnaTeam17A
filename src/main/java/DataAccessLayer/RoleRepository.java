package DataAccessLayer;

import DomainLayer.Market.Users.Roles.Role;
import DomainLayer.Market.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    List<Role> findByUser(User user);
    boolean existsByUser(UUID userId);

}