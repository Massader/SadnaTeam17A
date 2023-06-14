package DataAccessLayer;

import DomainLayer.Market.Users.ShoppingCart;
import DomainLayer.Market.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    boolean existsByUsername(String username);
    List<User> findByUsername(String lastName);
    List<User> findByUsernameContaining(String keyword);
//    boolean existsByClientCredentials(String clientCredentials);
//    List<User> findByClientCredentials(String clientCredentials);

    boolean existsByClientCredentials(UUID client_credentials);
//    public abstract boolean existsByCredentials(String credentials);
    List<User> findByClientCredentials(UUID client_credentials);


    boolean existsByIsAdmin(boolean isAdmin);
    //    public abstract boolean existsByCredentials(String credentials);
    List<User> findByIsAdmin(boolean isAdmin);
}
