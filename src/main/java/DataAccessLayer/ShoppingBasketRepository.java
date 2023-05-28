package DataAccessLayer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import DomainLayer.Market.Users.ShoppingBasket;

@Repository
public interface ShoppingBasketRepository extends JpaRepository< ShoppingBasket, Long> {
}
