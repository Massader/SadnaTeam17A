package DataAccessLayer;


import DomainLayer.Market.Stores.PurchaseTypes.PurchaseType;
import DomainLayer.Market.Users.Purchase;
import DomainLayer.Market.Users.ShoppingBasket;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PurchaseTypeRepository extends JpaRepository<PurchaseType, UUID> {
}
