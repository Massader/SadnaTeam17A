package DataAccessLayer;

import DomainLayer.Market.Stores.Store;
import DomainLayer.Market.Users.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;
@Repository
public interface StoreRepository extends JpaRepository<Store, UUID> {
    boolean existsByName(String name);
    List<User> findByName(String name);
    long countByClosedIsFalse();
    List<Store> findAllByClosedIsFalseAndShutdownIsFalse(Pageable pageable);
}
