package DataAccessLayer;

import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Users.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Long > {
    List<CartItem> findByItem(Item item);
}
