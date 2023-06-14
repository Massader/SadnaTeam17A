package DataAccessLayer;

import DomainLayer.Market.Stores.ItemReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ItemReviewRepository extends JpaRepository<ItemReview, UUID> {
}
