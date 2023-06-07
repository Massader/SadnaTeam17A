package DataAccessLayer;


import DomainLayer.Market.Users.ShoppingBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface  PurchaseRepository extends JpaRepository<ShoppingBasket, UUID> {
}
