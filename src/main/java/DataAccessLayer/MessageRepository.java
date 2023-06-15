package DataAccessLayer;


import DomainLayer.Market.Message;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface MessageRepository extends JpaRepository<Message, UUID> {
//    List<Message> findBySenderAndRecipient(UUID clientCredentials, UUID recipient);

    List<Message> findByRecipient(UUID recipient);
}
