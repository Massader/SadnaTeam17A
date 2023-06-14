package DataAccessLayer;


import DomainLayer.Market.Stores.Item;
import DomainLayer.Market.Stores.Store;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ItemRepository extends JpaRepository<Item, UUID> {
    List<Item> findAllByStore(UUID storeId);
}
